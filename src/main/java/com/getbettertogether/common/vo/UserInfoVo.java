package com.getbettertogether.common.vo;

import com.getbettertogether.entity.User;
import lombok.Data;

import java.util.List;

/**
 * 实现功能【用户信息 Vo】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:51:27
 */
@Data
public class UserInfoVo {
    private List<User> users;
}