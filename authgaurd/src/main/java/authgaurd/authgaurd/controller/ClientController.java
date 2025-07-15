package authgaurd.authgaurd.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import authgaurd.authgaurd.model.dto.ClientRequest;
import authgaurd.authgaurd.model.dto.ClientResponse;

@RestController
@RequestMapping("/client")
public class ClientController {
    @GetMapping("/login")
    public ResponseEntity<ClientResponse> getMethodName() {
        // Response<Ent
        return new ResponseEntity<>(new ClientResponse("ruddarm", "123"), HttpStatus.OK);
    }
    @PutMapping("/singup")
    public ResponseEntity<String> getMethodName(@RequestBody ClientRequest client) {
        return new ResponseEntity<>("fuck", HttpStatus.OK);
    }
    
}
