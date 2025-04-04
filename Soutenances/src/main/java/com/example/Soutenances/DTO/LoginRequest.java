package com.example.Soutenances.DTO;

import lombok.Getter;
import lombok.Setter;


public class LoginRequest {
    private String email;
    private String password;
    private String role;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
