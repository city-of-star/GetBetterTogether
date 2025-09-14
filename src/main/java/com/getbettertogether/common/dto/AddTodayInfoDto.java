package com.getbettertogether.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 实现功能【记录今日信息 Dto】
 *
 * @author li.hongyu
 * @date 2025-09-08 22:52:15
 */
@Data
public class AddTodayInfoDto {
    private Integer userId;
    private Double weight;
    private LocalDateTime date;
}