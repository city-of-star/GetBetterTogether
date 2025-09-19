package com.getbettertogether.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 实现功能【用户体重关联表】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:20:50
 */
@Data
@TableName("user_weight")
@Accessors(chain = true)
public class UserWeight {
    @TableId(type = IdType.AUTO)
    private Integer userWeightId;
    private Integer userId;
    private Double weight;
    private LocalDateTime date;
}