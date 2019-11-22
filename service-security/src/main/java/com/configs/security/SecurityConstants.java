package com.configs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

    @Value("${EXPIRATION_TIME}")
    public long EXPIRATION_TIME;
    @Value("${TOKEN_PREFIX}")
    public String TOKEN_PREFIX;
    @Value("${HEADER_STRING}")
    public String HEADER_STRING;
    @Value("${SECRET}")
    public String SECRET;

}