package com.authguard.authguard.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceException;
import com.authguard.authguard.model.domain.ClientAuth;
import com.authguard.authguard.model.domain.UserType;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {
    public final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    public ClientEntity saveClient(ClientEntity clientEntity) throws ResourceException {
        if (clientRepo.existsByEmail(clientEntity.getEmail())) {
            throw new ResourceException("Email Already Exist");
        }
        // if (clientRepo.existsByContactNumber(clientEntity.getContactNumber())) {
        //     throw new ResourceException("Contact Number Already exist");
        // }
        clientEntity.setHashPassword(passwordEncoder.encode(clientEntity.getHashPassword()));
        return clientRepo.save(clientEntity);
    }

    public Optional<ClientEntity> findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientEntity clientEntity = clientRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ClientAuth.builder().userId(clientEntity.getUserId()).username(clientEntity.getEmail())
                .password(clientEntity.getHashPassword()).userType(UserType.Client).build();

    }

    public Optional<ClientAuth> loadUserById(UUID id) {
        Optional<ClientEntity> clientEntityOptional = clientRepo.findById(id);

        if (clientEntityOptional.isPresent()) {
            ClientEntity clientEntity = clientEntityOptional.get();
            return Optional.of(ClientAuth.builder().userId(clientEntity.getUserId()).build());
        } else {
            return Optional.empty();
        }
    }
}
