package com.getbettertogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getbettertogether.entity.SportRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实现功能【运动记录表 Mapper】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:36:05
 */
@Mapper
public interface SportRecordMapper extends BaseMapper<SportRecord> {
}