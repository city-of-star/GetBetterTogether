package com.getbettertogether.utils;

import org.springframework.stereotype.Service;

/**
 * 实现功能【BMI计算工具类】
 *
 * @author li.hongyu
 * @date 2025-09-07 20:49:59
 */
@Service
public class BmiUtils {

    public static double calculateBmi(double height, double weight) {
        return weight / ((height / 100) * (height / 100));
    }

    public String evaluateHealthStatus(double bmi) {
        if (bmi < 18.5) {
            return "偏瘦";
        } else if (bmi < 24) {
            return "正常";
        } else if (bmi < 28) {
            return "超重";
        } else {
            return "肥胖";
        }
    }

    public String getBmiResult(double height, double weight) {
        double bmi = calculateBmi(weight, height);
        String status = evaluateHealthStatus(bmi);
        return String.format("您的BMI值为: %.2f, 健康状态: %s", bmi, status);
    }
}