package com.udemy.app.ui.controller;

import com.udemy.app.exceptions.UserServiceException;
import com.udemy.app.service.AddressService;
import com.udemy.app.service.UserService;
import com.udemy.app.shared.dto.AddressDto;
import com.udemy.app.shared.dto.UserDto;
import com.udemy.app.ui.model.request.UserDetailsRequestModel;
import com.udemy.app.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id)
    {
        UserDto userDto = userService.getUserById(id);
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(userDto, UserRest.class);
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue = new UserRest();

        if (userDetails.getEmail().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        //UserDto userDto = new UserDto();
        //BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
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
        ModelMapper modelMapper = new ModelMapper();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            returnValue.add(modelMapper.map(userDto, UserRest.class));
        }

        return returnValue;
    }

    @GetMapping(path = "/{id}/addresses")
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String id)
    {
        List<AddressRest> returnValue = new ArrayList<>();
        List<Link> linkList = new ArrayList<>();

        List<AddressDto> addressDto = addressService.getAddresses(id);

        if (addressDto != null && !addressDto.isEmpty())
        {
            Type listType = new TypeToken<List<AddressRest>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();

            returnValue = modelMapper.map(addressDto, listType);

            for (AddressRest address : returnValue)
            {
                Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserAddress(id, address.getAddressId())).withSelfRel();
                address.add(self);
            }
        }

        linkList.add(WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user"));
        linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddresses(id)).withSelfRel());

        return CollectionModel.of(returnValue, linkList);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId)
    {
        AddressDto addressDto = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();
        List<Link> linkList = new ArrayList<>();

        AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

        linkList.add(WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user"));
        linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddresses(userId)).withRel("addresses"));
        linkList.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddress(userId, addressId)).withSelfRel());

        return EntityModel.of(returnValue, linkList);
    }
}
