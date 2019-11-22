package com.configs.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.entities.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    private SecurityConstants securityConstants;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            System.out.println("request LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL = [" + request + "], response = [" + response + "]");
            Users creds = new ObjectMapper()
                    .readValue(request.getInputStream(), Users.class);
            System.out.println(creds);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e + " Probleme");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = ((User) authResult.getPrincipal());
        List<String> strings = new ArrayList<>();
        user.getAuthorities().forEach(grantedAuthority -> {
            strings.add(grantedAuthority.getAuthority());
        });
        String token = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withArrayClaim("roles", strings.toArray(new String[strings.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis() + securityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(securityConstants.SECRET.getBytes()));

        System.out.println("request authorization " + token);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        writer.append("{\"token\":\"" + token + "\"}");
        writer.flush();
        writer.close();
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);
        responseWrapper.addHeader(securityConstants.HEADER_STRING, securityConstants.TOKEN_PREFIX + token);
        chain.doFilter(request, responseWrapper);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("request unauthorization");
        response.setStatus(401);
        response.getWriter().append("cnx unauthorized");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
