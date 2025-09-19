package com.getbettertogether.controller;

import com.getbettertogether.common.dto.AddTodayInfoDto;
import com.getbettertogether.common.dto.AddUserDto;
import com.getbettertogether.common.dto.UpdateUserDto;
import com.getbettertogether.common.dto.UserWeightListDto;
import com.getbettertogether.common.dto.AddSportRecordDto;
import com.getbettertogether.common.dto.UserSportRecordListDto;
import com.getbettertogether.common.vo.UserWeightListVo;
import com.getbettertogether.common.vo.UserInfoVo;
import com.getbettertogether.common.vo.SportTreeVo;
import com.getbettertogether.common.vo.UserSportRecordListVo;
import com.getbettertogether.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 实现功能【用户服务 controller】
 * 提供用户管理、体重记录、运动记录等相关的REST API接口
 *
 * @author li.hongyu
 * @date 2025-08-24 00:16:28
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 添加新用户
     * 根据用户基本信息创建新用户，自动计算BMI值
     *
     * @param addUserDto 用户基本信息DTO，包含昵称、性别、年龄、身高、体重等信息
     */
    @PostMapping("/add")
    public void addUser(@RequestBody AddUserDto addUserDto) {
        userService.addUser(addUserDto);
    }

    /**
     * 更新用户信息
     * 根据用户ID更新用户的基本信息，重新计算BMI值
     *
     * @param updateUserDto 用户更新信息DTO，包含用户ID和要更新的基本信息
     */
    @PostMapping("/update")
    public void updateUser(@RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
    }

    /**
     * 获取所有用户信息
     * 查询并返回系统中所有用户的基本信息列表
     *
     * @return UserInfoVo 用户信息视图对象，包含用户列表
     */
    @GetMapping("/info")
    public UserInfoVo getUser() {
        return userService.getUsers();
    }

    /**
     * 添加今日信息
     * 记录用户当天的体重等健康数据
     *
     * @param addTodayInfoDto 今日信息DTO，包含用户ID、体重、记录日期等信息
     */
    @PostMapping("/setUserInfo")
    public void AddTodayInfo(@RequestBody AddTodayInfoDto addTodayInfoDto) {
        userService.addTodayInfo(addTodayInfoDto);
    }

    /**
     * 获取用户体重记录列表
     * 根据查询条件获取指定用户的体重变化历史记录
     *
     * @param dto 体重记录查询DTO，包含用户ID、查询时间范围等条件
     * @return UserWeightListVo 用户体重记录视图对象，包含体重记录列表
     */
    @PostMapping("/getUserWeightList")
    public UserWeightListVo getUserWeightList(@RequestBody UserWeightListDto dto) {
        return userService.getUserWeightList(dto);
    }

    /**
     * 获取运动项目树形结构
     * 获取所有运动项目的层级结构，用于前端展示运动分类
     *
     * @return SportTreeVo 运动项目树形结构视图对象
     */
    @GetMapping("/getSportTree")
    public SportTreeVo getSportTree() {
        return userService.getSportTree();
    }

    /**
     * 添加运动记录
     * 记录用户完成的运动项目及运动量
     *
     * @param addSportRecordDto 运动记录DTO，包含用户ID、运动项目、运动量、记录日期等信息
     */
    @PostMapping("/addSportRecord")
    public void addSportRecord(@RequestBody AddSportRecordDto addSportRecordDto) {
        userService.addSportRecord(addSportRecordDto);
    }

    /**
     * 获取用户运动记录列表
     * 根据查询条件获取指定用户的运动历史记录
     *
     * @param dto 运动记录查询DTO，包含用户ID、查询时间范围等条件
     * @return UserSportRecordListVo 用户运动记录视图对象，包含运动记录列表
     */
    @PostMapping("/getUserSportRecordList")
    public UserSportRecordListVo getUserSportRecordList(@RequestBody UserSportRecordListDto dto) {
        return userService.getUserSportRecordList(dto);
    }
}