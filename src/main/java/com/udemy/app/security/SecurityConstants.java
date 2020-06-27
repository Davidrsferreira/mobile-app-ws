package com.udemy.app.security;

import com.udemy.app.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";

    public static String getTokenSecret(){
        AppProperties properties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return properties.getTokenSecret();
    }
}