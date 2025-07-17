package com.authguard.authguard.services;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceFound;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    public final ClientRepository clientRepo;

    public ClientEntity saveClient(ClientEntity clientEntity) throws ResourceFound {
        if (clientRepo.existsByEmail(clientEntity.getEmail())) {
            throw new ResourceFound("Email Already Exist");
        }
        if(clientRepo.existsByContactNumber(clientEntity.getContactNumber())){
            throw new ResourceFound("Contact Number Already exist");
        }
        return clientRepo.save(clientEntity);
    }
}
