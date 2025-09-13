package com.getbettertogether.common.vo;

import com.getbettertogether.entity.UserWeight;
import lombok.Data;

import java.util.List;

/**
 * 实现功能【用户体重列表】
 *
 * @author li.hongyu
 * @date 2025-09-12 22:50:14
 */
@Data
public class UserWeightListVo {
    List<UserWeight> boyWeights;
    List<UserWeight> girlWeights;
}