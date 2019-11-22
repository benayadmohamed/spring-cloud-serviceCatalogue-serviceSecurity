package com.configs.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    private SecurityConstants securityConstants;

    public JwtAuthorisationFilter(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("httpServletRequestHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH = [" + securityConstants.HEADER_STRING + "]");
        String header = httpServletRequest.getHeader(securityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(securityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(httpServletRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(securityConstants.HEADER_STRING);
        if (token != null) {
            // parse the token.
            try {
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(securityConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(securityConstants.TOKEN_PREFIX, ""));

                String user = decodedJWT.getSubject();

                Claim claim = decodedJWT.getClaim("roles");
                List<GrantedAuthority> authorities = new ArrayList<>();
                for (int i = 0; i < claim.asArray(String.class).length; i++) {
                    authorities.add(new SimpleGrantedAuthority(claim.asArray(String.class)[i]));
                }
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            } catch (JWTVerificationException exception) {
                //Invalid signature/claims
                return null;
            }
        }
        return null;
    }
}
