package com.getbettertogether.common.dto;

import lombok.Data;

/**
 * 实现功能【修改用户 Dto】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:49:07
 */
@Data
public class UpdateUserDto {
    private Integer userId;
    private String nickname;
    private String sex;
    private Integer age;
    private Double initialWeight;
    private Double initialHeight;
}