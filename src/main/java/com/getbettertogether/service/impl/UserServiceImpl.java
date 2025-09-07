package com.getbettertogether.service.impl;

import com.getbettertogether.common.dto.AddUserDto;
import com.getbettertogether.common.dto.UpdateUserDto;
import com.getbettertogether.common.vo.UserInfoVo;
import com.getbettertogether.entity.User;
import com.getbettertogether.mapper.UserMapper;
import com.getbettertogether.service.UserService;

import com.getbettertogether.utils.BmiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 实现功能【用户服务实现类】
 *
 * @author li.hongyu
 * @date 2025-08-24 00:15:54
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void addUser(AddUserDto addUserDto) {
        User user = new User();
        user.setNickname(addUserDto.getNickname());
        user.setSex(addUserDto.getSex());
        user.setAge(addUserDto.getAge());
        user.setInitialHeight(addUserDto.getInitialHeight());
        user.setInitialWeight(addUserDto.getInitialWeight());
        user.setBmi(BmiUtils.calculateBmi(addUserDto.getInitialHeight(), addUserDto.getInitialWeight()));
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = new User();
        user.setUserId(updateUserDto.getUserId());
        user.setNickname(updateUserDto.getNickname());
        user.setSex(updateUserDto.getSex());
        user.setAge(updateUserDto.getAge());
        user.setInitialHeight(updateUserDto.getInitialHeight());
        user.setInitialWeight(updateUserDto.getInitialWeight());
        user.setBmi(BmiUtils.calculateBmi(updateUserDto.getInitialHeight(), updateUserDto.getInitialWeight()));
        userMapper.updateById(user);
    }

    @Override
    public UserInfoVo getUsers() {
        UserInfoVo vo = new UserInfoVo();
        vo.setUsers(userMapper.selectList(null));
        return vo;
    }
}