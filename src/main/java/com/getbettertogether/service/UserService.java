package com.getbettertogether.service;

import com.getbettertogether.common.dto.AddTodayInfoDto;
import com.getbettertogether.common.dto.AddUserDto;
import com.getbettertogether.common.dto.UpdateUserDto;
import com.getbettertogether.common.dto.UserWeightListDto;
import com.getbettertogether.common.vo.UserWeightListVo;
import com.getbettertogether.common.vo.UserInfoVo;

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
    void addTodayInfo(AddTodayInfoDto addTodayInfoDto);
    UserWeightListVo getUserWeightList(UserWeightListDto dto);
}