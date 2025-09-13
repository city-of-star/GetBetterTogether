-- 李鸿羽的运动记录（男性用户）
INSERT INTO get_better_together.sport_record (user_id, sport_id, date, core_value1, core_value2, notes)
SELECT 1, sport_id, '2025-08-01 07:30:00', 5, 30, '晨跑，感觉不错' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-02 18:00:00', 1000, 30, '游泳放松' FROM get_better_together.sport WHERE sport_name = '游泳' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-03 19:00:00', 60, 10, '健身房深蹲训练' FROM get_better_together.sport WHERE sport_name = '深蹲' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-04 19:00:00', 80, 8, '卧推进步了' FROM get_better_together.sport WHERE sport_name = '卧推' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-05 16:00:00', 90, NULL, '和朋友打篮球' FROM get_better_together.sport WHERE sport_name = '篮球' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-06 07:30:00', 4.5, 25, '有点累，跑得慢' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-07 08:00:00', 15, NULL, '骑行上班' FROM get_better_together.sport WHERE sport_name = '骑自行车' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-08 19:00:00', 100, 5, '硬拉新纪录' FROM get_better_together.sport WHERE sport_name = '硬拉' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-09 10:00:00', 60, NULL, '足球比赛' FROM get_better_together.sport WHERE sport_name = '足球' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-08-10 20:00:00', 45, NULL, '瑜伽放松' FROM get_better_together.sport WHERE sport_name = '瑜伽' AND is_directory = '1';

-- 刘齐慧的运动记录（女性用户）
INSERT INTO get_better_together.sport_record (user_id, sport_id, date, core_value1, core_value2, notes)
SELECT 2, sport_id, '2025-08-01 08:00:00', 3, 20, '晨跑开始' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-02 19:00:00', 200, NULL, '跳绳训练' FROM get_better_together.sport WHERE sport_name = '跳绳' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-03 18:00:00', 8, 3, '引体向上辅助训练' FROM get_better_together.sport WHERE sport_name = '引体向上' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-04 17:00:00', 60, NULL, '排球训练' FROM get_better_together.sport WHERE sport_name = '排球' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-05 19:00:00', 50, NULL, '普拉提课程' FROM get_better_together.sport WHERE sport_name = '普拉提' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-06 18:00:00', 800, 25, '游泳进步了' FROM get_better_together.sport WHERE sport_name = '游泳' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-07 19:00:00', 40, 12, '深蹲训练' FROM get_better_together.sport WHERE sport_name = '深蹲' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-08 20:00:00', 60, NULL, '瑜伽放松' FROM get_better_together.sport WHERE sport_name = '瑜伽' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-09 08:00:00', 3.5, 22, '跑步感觉很好' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-08-10 16:00:00', 120, NULL, '篮球练习' FROM get_better_together.sport WHERE sport_name = '篮球' AND is_directory = '1';

-- 李鸿羽的9月运动记录
INSERT INTO get_better_together.sport_record (user_id, sport_id, date, core_value1, core_value2, notes)
SELECT 1, sport_id, '2025-09-01 07:30:00', 5.5, 28, '跑步新路线' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-02 19:00:00', 85, 8, '卧推保持' FROM get_better_together.sport WHERE sport_name = '卧推' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-03 16:00:00', 120, NULL, '篮球比赛' FROM get_better_together.sport WHERE sport_name = '篮球' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-04 08:00:00', 20, NULL, '骑行上班' FROM get_better_together.sport WHERE sport_name = '骑自行车' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-05 19:00:00', 110, 4, '硬拉尝试新重量' FROM get_better_together.sport WHERE sport_name = '硬拉' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-06 18:00:00', 1200, 35, '游泳长距离' FROM get_better_together.sport WHERE sport_name = '游泳' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-07 20:00:00', 50, NULL, '瑜伽放松' FROM get_better_together.sport WHERE sport_name = '瑜伽' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-08 19:00:00', 70, 10, '深蹲训练' FROM get_better_together.sport WHERE sport_name = '深蹲' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-09 10:00:00', 90, NULL, '足球训练' FROM get_better_together.sport WHERE sport_name = '足球' AND is_directory = '1'
UNION ALL SELECT 1, sport_id, '2025-09-10 07:30:00', 6, 30, '跑步突破' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1';

-- 刘齐慧的9月运动记录
INSERT INTO get_better_together.sport_record (user_id, sport_id, date, core_value1, core_value2, notes)
SELECT 2, sport_id, '2025-09-01 08:00:00', 4, 25, '跑步进步' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-02 19:00:00', 60, NULL, '普拉提课程' FROM get_better_together.sport WHERE sport_name = '普拉提' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-03 18:00:00', 1000, 30, '游泳训练' FROM get_better_together.sport WHERE sport_name = '游泳' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-04 18:00:00', 10, 3, '引体向上进步' FROM get_better_together.sport WHERE sport_name = '引体向上' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-05 17:00:00', 90, NULL, '排球比赛' FROM get_better_together.sport WHERE sport_name = '排球' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-06 19:00:00', 45, 15, '深蹲训练' FROM get_better_together.sport WHERE sport_name = '深蹲' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-07 20:00:00', 60, NULL, '瑜伽放松' FROM get_better_together.sport WHERE sport_name = '瑜伽' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-08 19:00:00', 300, NULL, '跳绳训练' FROM get_better_together.sport WHERE sport_name = '跳绳' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-09 16:00:00', 150, NULL, '篮球练习' FROM get_better_together.sport WHERE sport_name = '篮球' AND is_directory = '1'
UNION ALL SELECT 2, sport_id, '2025-09-10 08:00:00', 4.5, 28, '跑步新纪录' FROM get_better_together.sport WHERE sport_name = '跑步' AND is_directory = '1';