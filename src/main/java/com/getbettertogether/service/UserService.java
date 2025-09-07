package com.getbettertogether.service;

import com.getbettertogether.common.dto.AddUserDto;
import com.getbettertogether.common.dto.UpdateUserDto;
import com.getbettertogether.common.vo.UserInfoVo;

import java.util.Map;

/**
 * 实现功能【用户服务】
 *
 * @author li.hongyu
 * @date 2025-08-24 00:15:43
 */
public interface UserService {
    void addUser(AddUserDto addUserDto);
    void updateUser(UpdateUserDto updateUserDto);
    UserInfoVo getUsers();
}