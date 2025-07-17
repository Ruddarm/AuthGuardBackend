package com.authguard.authguard.services;

import org.springframework.stereotype.Service;

import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    public final ClientRepository clientRepo;
    public ClientEntity saveClient(ClientEntity clientEntity) {
        return clientRepo.save(clientEntity);
    }
}
