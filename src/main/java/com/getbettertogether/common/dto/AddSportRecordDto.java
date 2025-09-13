package com.getbettertogether.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实现功能【添加运动记录 Dto】
 *
 * @author li.hongyu
 * @date 2025-01-27 10:30:00
 */
@Data
public class AddSportRecordDto {
    private Integer userId;
    private Integer sportId;
    private LocalDateTime date;
    private Double coreValue1;
    private Double coreValue2;
    private String notes;
}
