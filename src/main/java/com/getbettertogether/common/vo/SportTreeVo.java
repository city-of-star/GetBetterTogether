package com.getbettertogether.common.vo;

import com.getbettertogether.entity.Sport;
import lombok.Data;

import java.util.List;

/**
 * 实现功能【运动定义树 Vo】
 *
 * @author li.hongyu
 * @date 2025-01-27 10:00:00
 */
@Data
public class SportTreeVo {
    private List<SportTreeNode> sportTree;
    
    @Data
    public static class SportTreeNode {
        private Integer sportId;
        private String sportName;
        private String coreName1;
        private String coreName2;
        private String coreUnit1;
        private String coreUnit2;
        private Integer fatherId;
        private Integer sequence;
        private String isDirectory;
        private List<SportTreeNode> children;
    }
}
