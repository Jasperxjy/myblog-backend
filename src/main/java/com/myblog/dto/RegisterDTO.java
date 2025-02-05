package com.myblog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
    private String userName;
    private String role;
}