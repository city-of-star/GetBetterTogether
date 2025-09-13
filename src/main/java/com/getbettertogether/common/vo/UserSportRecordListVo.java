package com.getbettertogether.common.vo;

import com.getbettertogether.entity.SportRecord;
import lombok.Data;

import java.util.List;

/**
 * 实现功能【用户运动记录列表】
 *
 * @author li.hongyu
 * @date 2025-01-27 10:00:00
 */
@Data
public class UserSportRecordListVo {
    List<SportRecord> boySportRecords;
    List<SportRecord> girlSportRecords;
}
