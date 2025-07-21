package com.authguard.authguard.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.authguard.authguard.model.dto.UserRequest;
import com.authguard.authguard.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserEntity userRequestToUserEntity(UserRequest userRequest) {
        UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);
        userEntity.setPasswordHash(userRequest.getPassword());
        return userEntity;
    }

}
