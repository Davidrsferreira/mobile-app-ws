package com.udemy.app.ui.controller;

import com.udemy.app.ui.model.request.UserDatailsRequestModel;
import com.udemy.app.ui.model.response.UserRest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @GetMapping
    public String getUser()
    {
        return "get users was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDatailsRequestModel userDetails)
    {
        return null;
    }

    @PutMapping
    public String updateUser()
    {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser()
    {
        return "delete user was called";
    }
}
