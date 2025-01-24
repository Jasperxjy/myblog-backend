package com.myblog.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String userName;
    private String role;
}