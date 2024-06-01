package com.photogallery.photogallery.Request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 500)
    private String username;
 
    @NotBlank
    @Size(max = 500)
    @Email
    private String email;
      
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String passcode;
}
