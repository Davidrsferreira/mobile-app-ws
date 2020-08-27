package com.udemy.app.service.impl;

import com.udemy.app.io.entity.AddressEntity;
import com.udemy.app.io.entity.UserEntity;
import com.udemy.app.io.repositories.AddressRepository;
import com.udemy.app.io.repositories.UserRepository;
import com.udemy.app.service.AddressService;
import com.udemy.app.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {

        List<AddressDto> returnValue = new ArrayList<>();

        UserEntity user = userRepository.findByUserId(userId);

        Iterable<AddressEntity> address = addressRepository.findAllByUserDetails(user);
        ModelMapper modelMapper = new ModelMapper();

        for (AddressEntity addressEntity : address)
        {
            returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {

        AddressDto returnValue = null;
        ModelMapper modelMapper = new ModelMapper();

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null)
        {
            returnValue = modelMapper.map(addressEntity, AddressDto.class);
        }

        return returnValue;
    }
}
