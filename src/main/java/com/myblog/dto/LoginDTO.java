package com.myblog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
}