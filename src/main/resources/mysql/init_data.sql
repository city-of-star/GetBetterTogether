-- 1. 保存user_weight表中的数据到临时表
CREATE TEMPORARY TABLE temp_user_weight AS
SELECT * FROM get_better_together.user_weight;

-- 2. 删除所有表（注意删除顺序以避免外键约束问题）
DROP TABLE IF EXISTS get_better_together.sport_record;
DROP TABLE IF EXISTS get_better_together.user_weight;
DROP TABLE IF EXISTS get_better_together.user;
DROP TABLE IF EXISTS get_better_together.sport;

-- 3. 重新创建表结构
CREATE TABLE get_better_together.sport
(
    sport_id     int auto_increment comment '项目ID'
        primary key,
    sport_name   varchar(100) null comment '项目名称',
    core_name_1  varchar(100) null comment '核心名称1',
    core_name_2  varchar(100) null comment '核心名称2',
    core_unit_1  varchar(100) null comment '核心单位1',
    core_unit_2  varchar(100) null comment '核心单位2',
    father_id    int          null comment '父运动项目ID（顶层为0）',
    `order`      int          null comment '顺序',
    is_directory varchar(5)   null comment '是否是目录（0：是，1：不是）',
    constraint fk_sport_father
        foreign key (father_id) references get_better_together.sport (sport_id)
            on update cascade on delete cascade
)
    comment '运动项目定义表';

CREATE TABLE get_better_together.user
(
    user_id        int auto_increment comment '用户ID'
        primary key,
    nickname       varchar(50) null comment '昵称',
    sex            varchar(5)  null comment '性别（0：女，1：男）',
    age            int         null comment '年龄',
    initial_weight double      null comment '初始体重（kg）',
    initial_height double      null comment '初始身高(cm)',
    bmi            double      null comment 'BMI指数'
)
    comment '用户信息表';

CREATE TABLE get_better_together.sport_record
(
    record_id    int auto_increment comment '运动记录ID'
        primary key,
    user_id      int      null comment '用户ID',
    sport_id     int      null comment '运动项目ID',
    date         datetime null comment '日期',
    core_value_1 double   null comment '核心数值1',
    core_value_2 double   null comment '核心数值2',
    notes        text     null comment '用户备注',
    constraint fk_record_sport
        foreign key (sport_id) references get_better_together.sport (sport_id)
            on update cascade on delete cascade,
    constraint fk_record_user
        foreign key (user_id) references get_better_together.user (user_id)
            on update cascade on delete cascade
)
    comment '运动记录表';

CREATE TABLE get_better_together.user_weight
(
    user_weight_id int auto_increment comment '唯一ID'
        primary key,
    user_id        int      null comment '用户id',
    weight         double   null comment '体重(kg)',
    date           datetime null comment '日期',
    constraint fk_weight_user
        foreign key (user_id) references get_better_together.user (user_id)
            on update cascade on delete cascade
)
    comment '用户体重关联表';

-- 4. 执行后续的插入操作
-- 添加男性用户
INSERT INTO get_better_together.user (nickname, sex, age, initial_weight, initial_height, bmi)
VALUES ('李鸿羽', '1', 22, 69.5, 184.0, ROUND(69.5 / ((184.0/100) * (184.0/100)), 2));

-- 添加女性用户
INSERT INTO get_better_together.user (nickname, sex, age, initial_weight, initial_height, bmi)
VALUES ('刘齐慧', '0', 22, 57.5, 156.0, ROUND(57.5 / ((156.0/100) * (156.0/100)), 2));

-- 5. 恢复user_weight数据（重置自增ID从1开始）
INSERT INTO get_better_together.user_weight (user_id, weight, date)
SELECT user_id, weight, date FROM temp_user_weight
ORDER BY user_weight_id;

-- 6. 删除临时表
DROP TEMPORARY TABLE IF EXISTS temp_user_weight;

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