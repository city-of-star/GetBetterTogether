create table get_better_together.sport
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

create table get_better_together.user
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

create table get_better_together.sport_record
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

create table get_better_together.user_weight
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


-- 添加男性用户
INSERT INTO get_better_together.user (nickname, sex, age, initial_weight, initial_height, bmi)
VALUES ('李鸿羽', '1', 22, 69.5, 184.0, ROUND(69.5 / ((184.0/100) * (184.0/100)), 2));

-- 添加女性用户
INSERT INTO get_better_together.user (nickname, sex, age, initial_weight, initial_height, bmi)
VALUES ('刘齐慧', '0', 22, 57.5, 156.0, ROUND(57.5 / ((156.0/100) * (156.0/100)), 2));