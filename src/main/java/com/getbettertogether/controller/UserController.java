package com.getbettertogether.controller;

import com.getbettertogether.common.dto.AddUserDto;
import com.getbettertogether.common.dto.UpdateUserDto;
import com.getbettertogether.common.vo.UserInfoVo;
import com.getbettertogether.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 实现功能【用户服务 controller】
 *
 * @author li.hongyu
 * @date 2025-08-24 00:16:28
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public void addUser(@RequestBody AddUserDto addUserDto) {
        userService.addUser(addUserDto);
    }

    @PostMapping("/update")
    public void updateUser(@RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
    }

    @GetMapping("/info")
    public UserInfoVo getUser() {
        return userService.getUsers();
    }
}