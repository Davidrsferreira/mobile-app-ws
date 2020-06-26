package com.udemy.app.shared.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

    private static final long serialVersionUID = -5701958360870313026L;
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;

}
