package com.authguard.authguard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String accessToken;
    public UserResponse(String fname, String lname,String email){
        this.firstName= fname;
        this.lastName = lname;
        this.email = email;
    }
}
