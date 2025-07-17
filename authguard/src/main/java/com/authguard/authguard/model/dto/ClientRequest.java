package com.authguard.authguard.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 

public class ClientRequest {
    @NotBlank(message="Name is required")
    String name;
    @NotBlank(message="Password is required" )
    String password;
    String country;
    @Email(message="Email is not valid")
    String email;
    @NotBlank(message="Contact Number is not valid")
    @Size(min=10,max=10,message="Contact Number is upto 10 digit only")
    String contactNumber;
}
