package com.getbettertogether.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实现功能【用户实体】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:11:07
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String nickname;
    private String sex;
    private Integer age;
    private Double initialWeight;
    private Double initialHeight;
    private Double bmi;
}