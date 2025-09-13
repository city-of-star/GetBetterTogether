package com.getbettertogether.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实现功能【运动项目定义实体】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:14:06
 */
@Data
@TableName("sport")
public class Sport {
    @TableId(type = IdType.AUTO)
    private Integer sportId;
    private String sportName;
    private String coreName1;
    private String coreName2;
    private String coreUnit1;
    private String coreUnit2;
    private Integer fatherId;
    private Integer sequence;
    private String isDirectory;
}