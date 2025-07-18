package com.authguard.authguard.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.domain.ClientUser;
import com.authguard.authguard.model.entity.ClientEntity;

import ch.qos.logback.core.net.server.Client;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClientUserService implements UserDetailsService {
    private final ClientService clientService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientEntity clientEntity = clientService.clientRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new ClientUser(clientEntity.getClientId(), clientEntity.getEmail(), clientEntity.getHashPassword());

    }

    public Optional<ClientUser> loadUserById(UUID id) {
        Optional<ClientEntity> clientEntityOptional = clientService.clientRepo.findById(id);

        if (clientEntityOptional.isPresent()) {
            ClientEntity clientEntity = clientEntityOptional.get();
            return Optional.of(ClientUser.builder().userId(clientEntity.getClientId()).build());
        } else {
            return Optional.empty();
        }
    }

}
