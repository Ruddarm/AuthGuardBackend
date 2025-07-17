package com.authguard.authguard.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message="Name is empty")
    @NotNull(message="Name is required")
    @Size(min=2)
    String name;
    @NotBlank(message="Password is empty" )
    @NotNull(message="password is requird")
    @Size(min=8)
    String password;
    String country;
    @Email(message="Email is not valid")
    @NotBlank(message="Email is empty")
    @NotNull(message="Email is required")
    String email;
    @NotBlank(message="Contact Number is not valid")
    @NotNull(message="Contact number is required")
    @Size(min=10,max=10,message="Contact Number is upto 10 digit only")
    String contactNumber;
}
