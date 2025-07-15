package authgaurd.authgaurd.model.dto;

import lombok.Data;

@Data
public class ClientResponse {
    String name;
    String id;
    public ClientResponse(String name, String id){
        this.name=name;
        this.id=id;
    }

}
