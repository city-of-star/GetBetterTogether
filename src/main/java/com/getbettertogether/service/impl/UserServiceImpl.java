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

    @Override
    public void addTodayInfo(AddTodayInfoDto addTodayInfoDto) {
        // 获取今天的开始和结束时间
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        
        // 查询今天是否已经记录过体重
        QueryWrapper<UserWeight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", addTodayInfoDto.getUserId())
                   .between("date", startOfDay, endOfDay);
        
        UserWeight existingWeight = userWeightMapper.selectOne(queryWrapper);
        
        if (existingWeight != null) {
            // 如果今天已经记录过，更新体重
            existingWeight.setWeight(addTodayInfoDto.getWeight());
            existingWeight.setDate(LocalDateTime.now());
            userWeightMapper.updateById(existingWeight);
        } else {
            // 如果今天没有记录过，新增记录
            UserWeight userWeight = new UserWeight();
            userWeight.setUserId(addTodayInfoDto.getUserId());
            userWeight.setWeight(addTodayInfoDto.getWeight());
            userWeight.setDate(LocalDateTime.now());
            userWeightMapper.insert(userWeight);
        }
    }

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
}