package com.udemy.app.ui.controller;

import com.udemy.app.exceptions.UserServiceException;
import com.udemy.app.service.UserService;
import com.udemy.app.shared.dto.UserDto;
import com.udemy.app.ui.model.request.UserDatailsRequestModel;
import com.udemy.app.ui.model.response.ErrorMessages;
import com.udemy.app.ui.model.response.OperationStatusModel;
import com.udemy.app.ui.model.response.RequestOperationStatus;
import com.udemy.app.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id)
    {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserById(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDatailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if (userDetails.getEmail().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDatailsRequestModel userDetails)
    {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id)
    {
        OperationStatusModel returnValue = new OperationStatusModel();

        returnValue.setName(RequestOperationStatus.DELETE.name());
        userService.deleteUser(id);
        returnValue.setStatus(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }
}
