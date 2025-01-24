package com.myblog.utility;

import lombok.Data;

@Data
public class UserStatus {
    public static Integer PENDING=0;
    public static Integer NORMAL=1;
    public static Integer DELETED=3;
    public static Integer BANNED=2;
}
