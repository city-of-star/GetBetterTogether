package com.getbettertogether.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实现功能【运动记录表】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:16:49
 */
@Data
@TableName("sport_record")
public class SportRecord {
    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private Integer sportId;
    private LocalDateTime date;
    private Double coreValue1;
    private Double coreValue2;
    private String notes;
}