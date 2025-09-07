package com.getbettertogether.common.dto;

import lombok.Data;

/**
 * 实现功能【新增用户 Dto】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:47:07
 */
@Data
public class AddUserDto {
    private String nickname;
    private String sex;
    private Integer age;
    private Double initialWeight;
    private Double initialHeight;
}