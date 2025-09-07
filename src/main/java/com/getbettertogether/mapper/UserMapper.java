package com.getbettertogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getbettertogether.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实现功能【用户实体 Mapper】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:34:28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}