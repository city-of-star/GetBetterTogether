package com.getbettertogether;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 实现功能【工具测试】
 *
 * @author li.hongyu
 * @date 2025-08-24 18:52:34
 */
public class UtilsTest {

    /**
     * 密码加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void fun() { System.out.println(encryptPassword("123456")); }
}