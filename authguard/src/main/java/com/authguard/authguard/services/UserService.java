package com.authguard.authguard.services;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.UserAuth;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.dto.UserResponse;
import com.authguard.authguard.model.entity.UserEntity;
import com.authguard.authguard.repository.UserRepostiory;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepostiory userRepostiory;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserResponse saveUser(UserEntity userEntity) throws ResourceException {
        if (userRepostiory.existsByEmail(userEntity.getEmail())) {
            throw new ResourceException("Email already exists");
        }
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash()));
        userEntity = userRepostiory.save(userEntity);
        System.out.println("fuck in dde dto");
        return modelMapper.map(userEntity, UserResponse.class);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepostiory.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserAuth.builder().userId(user.getUserId()).password(user.getPasswordHash())
                .username(user.getEmail()).userType(UserType.User).build();
    }

    public Optional<UserAuth> loadUserById(UUID id) {
        Optional<UserEntity> userEntityOptional = userRepostiory.findById(id);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            return Optional
                    .of(UserAuth.builder().userId(userEntity.getUserId()).username(userEntity.getEmail()).build());
        } else {
            return Optional.empty();
        }
    }
}