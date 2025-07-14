package authgaurd.authgaurd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/login")
    public String getMethodName() {
        return new String("Loged in here");
    }
    
}
