package com.getbettertogether.service.impl;

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
import com.getbettertogether.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.getbettertogether.entity.UserWeight;
import com.getbettertogether.entity.Sport;
import com.getbettertogether.entity.SportRecord;
import com.getbettertogether.mapper.UserMapper;
import com.getbettertogether.mapper.UserWeightMapper;
import com.getbettertogether.mapper.SportMapper;
import com.getbettertogether.mapper.SportRecordMapper;
import com.getbettertogether.service.UserService;

import com.getbettertogether.utils.BmiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


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
    private final UserWeightMapper userWeightMapper;
    private final SportMapper sportMapper;
    private final SportRecordMapper sportRecordMapper;

    /**
     * 添加新用户
     * 根据用户基本信息创建新用户，自动计算BMI值
     *
     * @param addUserDto 用户基本信息DTO，包含昵称、性别、年龄、身高、体重等信息
     */
    @Override
    public void addUser(AddUserDto addUserDto) {
        User user = new User()
                .setNickname(addUserDto.getNickname())
                .setSex(addUserDto.getSex())
                .setAge(addUserDto.getAge())
                .setInitialHeight(addUserDto.getInitialHeight())
                .setInitialWeight(addUserDto.getInitialWeight())
                .setBmi(BmiUtils.calculateBmi(addUserDto.getInitialHeight(), addUserDto.getInitialWeight()));
        userMapper.insert(user);
    }

    /**
     * 更新用户信息
     * 根据用户ID更新用户的基本信息，重新计算BMI值
     *
     * @param updateUserDto 用户更新信息DTO，包含用户ID和要更新的基本信息
     */
    @Override
    public void updateUser(UpdateUserDto updateUserDto) {
        User user = new User()
                .setUserId(updateUserDto.getUserId())
                .setNickname(updateUserDto.getNickname())
                .setSex(updateUserDto.getSex())
                .setAge(updateUserDto.getAge())
                .setInitialHeight(updateUserDto.getInitialHeight())
                .setInitialWeight(updateUserDto.getInitialWeight())
                .setBmi(BmiUtils.calculateBmi(updateUserDto.getInitialHeight(), updateUserDto.getInitialWeight()));
        userMapper.updateById(user);
    }

    /**
     * 获取所有用户信息
     * 查询并返回系统中所有用户的基本信息列表
     *
     * @return UserInfoVo 用户信息视图对象，包含用户列表
     */
    @Override
    public UserInfoVo getUsers() {
        UserInfoVo vo = new UserInfoVo();
        vo.setUsers(userMapper.selectList(null));
        return vo;
    }

    /**
     * 添加今日信息
     * 记录用户当天的体重等健康数据
     *
     * @param addTodayInfoDto 今日信息DTO，包含用户ID、体重、记录日期等信息
     */
    @Override
    public void addTodayInfo(AddTodayInfoDto addTodayInfoDto) {
        // 使用传入的日期，如果没有传入则使用当前时间
        LocalDateTime targetDate = addTodayInfoDto.getDate() != null ? addTodayInfoDto.getDate() : LocalDateTime.now();
        LocalDate targetDateOnly = targetDate.toLocalDate();
        LocalDateTime startOfDay = targetDateOnly.atStartOfDay();
        LocalDateTime endOfDay = targetDateOnly.atTime(23, 59, 59);
        
        // 查询指定日期是否已经记录过体重
        QueryWrapper<UserWeight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", addTodayInfoDto.getUserId())
                   .between("date", startOfDay, endOfDay);
        
        UserWeight existingWeight = userWeightMapper.selectOne(queryWrapper);
        
        if (existingWeight != null) {
            // 如果指定日期已经记录过，更新体重
            existingWeight.setWeight(addTodayInfoDto.getWeight());
            existingWeight.setDate(targetDate);
            userWeightMapper.updateById(existingWeight);
        } else {
            // 如果指定日期没有记录过，新增记录
            UserWeight userWeight = new UserWeight();
            userWeight.setUserId(addTodayInfoDto.getUserId());
            userWeight.setWeight(addTodayInfoDto.getWeight());
            userWeight.setDate(targetDate);
            userWeightMapper.insert(userWeight);
        }
    }

    /**
     * 获取用户体重记录列表
     * 根据查询条件获取指定用户的体重变化历史记录
     *
     * @param dto 体重记录查询DTO，包含用户ID、查询时间范围等条件
     * @return UserWeightListVo 用户体重记录视图对象，包含体重记录列表
     */
    @Override
    public UserWeightListVo getUserWeightList(UserWeightListDto dto) {
        UserWeightListVo res = new UserWeightListVo();

        // 获取当前年份
        int currentYear = LocalDate.now().getYear();

        // 根据传入的月份值构建查询的月份范围
        YearMonth targetMonth = YearMonth.of(currentYear, dto.getMonth());
        LocalDateTime startDate = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        // 查询男孩（user_id=1）的体重记录
        List<UserWeight> boyWeights = userWeightMapper.selectList(
                new QueryWrapper<UserWeight>()
                        .eq("user_id", 1)
                        .ge("date", startDate)  // 大于等于月份第一天
                        .le("date", endDate)    // 小于等于月份最后一天
                        .orderByAsc("date")     // 按日期升序排列
        );

        // 查询女孩（user_id=2）的体重记录
        List<UserWeight> girlWeights = userWeightMapper.selectList(
                new QueryWrapper<UserWeight>()
                        .eq("user_id", 2)
                        .ge("date", startDate)  // 大于等于月份第一天
                        .le("date", endDate)    // 小于等于月份最后一天
                        .orderByAsc("date")     // 按日期升序排列
        );

        res.setBoyWeights(boyWeights);
        res.setGirlWeights(girlWeights);
        return res;
    }

    /**
     * 获取运动项目树形结构
     * 获取所有运动项目的层级结构，用于前端展示运动分类
     *
     * @return SportTreeVo 运动项目树形结构视图对象
     */
    @Override
    public SportTreeVo getSportTree() {
        SportTreeVo result = new SportTreeVo();
        
        // 查询所有运动项目
        List<Sport> allSports = sportMapper.selectList(null);
        
        // 按父ID分组
        Map<Integer, List<Sport>> sportsByFatherId = allSports.stream()
                .collect(Collectors.groupingBy(sport -> sport.getFatherId() == null ? 0 : sport.getFatherId()));
        
        // 构建树结构
        List<SportTreeVo.SportTreeNode> rootNodes = buildSportTree(sportsByFatherId, 0);
        result.setSportTree(rootNodes);
        
        return result;
    }

    /**
     * 添加运动记录
     * 记录用户完成的运动项目及运动量
     *
     * @param addSportRecordDto 运动记录DTO，包含用户ID、运动项目、运动量、记录日期等信息
     */
    @Override
    public void addSportRecord(AddSportRecordDto addSportRecordDto) {
        SportRecord sportRecord = new SportRecord();
        sportRecord.setUserId(addSportRecordDto.getUserId());
        sportRecord.setSportId(addSportRecordDto.getSportId());
        sportRecord.setDate(addSportRecordDto.getDate() != null ? addSportRecordDto.getDate() : LocalDateTime.now());
        sportRecord.setCoreValue1(addSportRecordDto.getCoreValue1());
        sportRecord.setCoreValue2(addSportRecordDto.getCoreValue2());
        sportRecord.setNotes(addSportRecordDto.getNotes());
        sportRecordMapper.insert(sportRecord);
    }

    /**
     * 获取用户运动记录列表
     * 根据查询条件获取指定用户的运动历史记录
     *
     * @param dto 运动记录查询DTO，包含用户ID、查询时间范围等条件
     * @return UserSportRecordListVo 用户运动记录视图对象，包含运动记录列表
     */
    @Override
    public UserSportRecordListVo getUserSportRecordList(UserSportRecordListDto dto) {
        UserSportRecordListVo res = new UserSportRecordListVo();

        // 获取当前年份
        int currentYear = LocalDate.now().getYear();

        // 根据传入的月份值构建查询的月份范围
        YearMonth targetMonth = YearMonth.of(currentYear, dto.getMonth());
        LocalDateTime startDate = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        // 查询男孩（user_id=1）的运动记录
        List<SportRecord> boySportRecords = sportRecordMapper.selectList(
                new QueryWrapper<SportRecord>()
                        .eq("user_id", 1)
                        .ge("date", startDate)  // 大于等于月份第一天
                        .le("date", endDate)    // 小于等于月份最后一天
                        .orderByAsc("date")     // 按日期升序排列
        );

        // 查询女孩（user_id=2）的运动记录
        List<SportRecord> girlSportRecords = sportRecordMapper.selectList(
                new QueryWrapper<SportRecord>()
                        .eq("user_id", 2)
                        .ge("date", startDate)  // 大于等于月份第一天
                        .le("date", endDate)    // 小于等于月份最后一天
                        .orderByAsc("date")     // 按日期升序排列
        );

        res.setBoySportRecords(boySportRecords);
        res.setGirlSportRecords(girlSportRecords);
        return res;
    }

    /**
     * 构建运动项目树形结构
     * 递归构建运动项目的层级树形结构，按sequence字段排序
     *
     * @param sportsByFatherId 按父ID分组的运动项目映射
     * @param fatherId 父节点ID，0表示根节点
     * @return 运动项目树节点列表
     */
    private List<SportTreeVo.SportTreeNode> buildSportTree(Map<Integer, List<Sport>> sportsByFatherId, Integer fatherId) {
        List<Sport> children = sportsByFatherId.get(fatherId);
        if (children == null || children.isEmpty()) {
            return new ArrayList<>();
        }
        
        return children.stream()
                .sorted((s1, s2) -> {
                    if (s1.getSequence() == null && s2.getSequence() == null) return 0;
                    if (s1.getSequence() == null) return 1;
                    if (s2.getSequence() == null) return -1;
                    return s1.getSequence().compareTo(s2.getSequence());
                })
                .map(sport -> {
                    SportTreeVo.SportTreeNode node = new SportTreeVo.SportTreeNode();
                    node.setSportId(sport.getSportId());
                    node.setSportName(sport.getSportName());
                    node.setCoreName1(sport.getCoreName1());
                    node.setCoreName2(sport.getCoreName2());
                    node.setCoreUnit1(sport.getCoreUnit1());
                    node.setCoreUnit2(sport.getCoreUnit2());
                    node.setFatherId(sport.getFatherId());
                    node.setSequence(sport.getSequence());
                    node.setIsDirectory(sport.getIsDirectory());
                    
                    // 递归构建子节点
                    List<SportTreeVo.SportTreeNode> childNodes = buildSportTree(sportsByFatherId, sport.getSportId());
                    node.setChildren(childNodes);
                    
                    return node;
                })
                .collect(Collectors.toList());
    }
}