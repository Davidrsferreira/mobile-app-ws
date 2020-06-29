package com.udemy.app.service;

import com.udemy.app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserById(String id);
    UserDto updateUser(String id, UserDto userDto);
    void deleteUser(String id);
}
