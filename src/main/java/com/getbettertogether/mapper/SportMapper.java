package com.getbettertogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getbettertogether.entity.Sport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实现功能【运动项目定义实体 Mapper】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:35:18
 */
@Mapper
public interface SportMapper extends BaseMapper<Sport> {
}