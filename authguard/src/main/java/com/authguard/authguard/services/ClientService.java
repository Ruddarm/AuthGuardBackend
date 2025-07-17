package com.authguard.authguard.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceFound;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    public final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    public ClientEntity saveClient(ClientEntity clientEntity) throws ResourceFound {
        if (clientRepo.existsByEmail(clientEntity.getEmail())) {
            throw new ResourceFound("Email Already Exist");
        }
        if (clientRepo.existsByContactNumber(clientEntity.getContactNumber())) {
            throw new ResourceFound("Contact Number Already exist");
        }
        clientEntity.setHashPassword(passwordEncoder.encode(clientEntity.getHashPassword()));
        return clientRepo.save(clientEntity);
    }

    public Optional<ClientEntity> findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }
}
