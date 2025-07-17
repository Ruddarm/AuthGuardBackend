package com.authguard.authguard.services;

import org.springframework.stereotype.Service;

import com.authguard.authguard.Exception.ResourceExist;
import com.authguard.authguard.model.entity.ClientEntity;
import com.authguard.authguard.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    public final ClientRepository clientRepo;

    public ClientEntity saveClient(ClientEntity clientEntity) throws ResourceExist {
        if (clientRepo.existsByEmail(clientEntity.getEmail())) {
            throw new ResourceExist("Email Already Exist");
        }
        if(clientRepo.existsByContactNumber(clientEntity.getContactNumber())){
            throw new ResourceExist("Contact Number Already exist");
        }
        return clientRepo.save(clientEntity);
    }
}
