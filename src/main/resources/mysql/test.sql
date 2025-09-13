-- 首先禁用外键检查（仅在当前会话中）
SET FOREIGN_KEY_CHECKS = 0;

-- 插入顶层目录（父ID设为NULL）
INSERT INTO get_better_together.sport (sport_name, father_id, `order`, is_directory)
VALUES ('有氧运动', NULL, 1, '0');

INSERT INTO get_better_together.sport (sport_name, father_id, `order`, is_directory)
VALUES ('力量训练', NULL, 2, '0');

INSERT INTO get_better_together.sport (sport_name, father_id, `order`, is_directory)
VALUES ('团队运动', NULL, 3, '0');

INSERT INTO get_better_together.sport (sport_name, father_id, `order`, is_directory)
VALUES ('其他运动', NULL, 4, '0');

-- 获取顶层目录的ID
SET @aerobic_id = (SELECT sport_id FROM get_better_together.sport WHERE sport_name = '有氧运动');
SET @strength_id = (SELECT sport_id FROM get_better_together.sport WHERE sport_name = '力量训练');
SET @team_id = (SELECT sport_id FROM get_better_together.sport WHERE sport_name = '团队运动');
SET @other_id = (SELECT sport_id FROM get_better_together.sport WHERE sport_name = '其他运动');

-- 插入有氧运动子项
INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('跑步', '距离', '公里', '时间', '分钟', @aerobic_id, 1, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('游泳', '距离', '米', '时间', '分钟', @aerobic_id, 2, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('骑自行车', '距离', '公里', @aerobic_id, 3, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('跳绳', '次数', '次', @aerobic_id, 4, '1');

-- 插入力量训练子项
INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('深蹲', '重量', 'kg', '次数', '次', @strength_id, 1, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('卧推', '重量', 'kg', '次数', '次', @strength_id, 2, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('硬拉', '重量', 'kg', '次数', '次', @strength_id, 3, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, core_name_2, core_unit_2, father_id, `order`, is_directory)
VALUES ('引体向上', '次数', '次', '组数', '组', @strength_id, 4, '1');

-- 插入团队运动子项
INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('篮球', '时间', '分钟', @team_id, 1, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('足球', '时间', '分钟', @team_id, 2, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('排球', '时间', '分钟', @team_id, 3, '1');

-- 插入其他运动子项
INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('瑜伽', '时间', '分钟', @other_id, 1, '1');

INSERT INTO get_better_together.sport (sport_name, core_name_1, core_unit_1, father_id, `order`, is_directory)
VALUES ('普拉提', '时间', '分钟', @other_id, 2, '1');

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;