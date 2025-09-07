package com.getbettertogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getbettertogether.entity.UserWeight;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实现功能【用户体重关联表 Mapper】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:36:37
 */
@Mapper
public interface UserWeightMapper extends BaseMapper<UserWeight> {
}