package com.myblog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class UserInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userName;
    private String email;
    private String userRole;
    private Integer status;
}
