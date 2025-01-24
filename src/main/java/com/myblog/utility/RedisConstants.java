package com.myblog.utility;

/**
 * @author 熊
 * 作用：定义redis中的一些常量
 */
public class RedisConstants {

    //用户登录token头
    public static final String LOGIN_TOKEN="user:token:";
    //用户登录id
    public static final String LOGIN_ID="user:id:";
    //用户登录token有效期分钟数，含义为操作一次，有效用户信息在redis中存储的时长，即在登录状态下操作一次可以保持多久的登录状态
    public static final long LOGIN_LIMIT_TIME = 30L;
    //用户token有效期毫秒数
    public static final long LOGIN_LIMIT_TIME_MIL = 1800000L;
    //用户token加密用盐
    public static final String LOGIN_SALT = "users_salt";


}
