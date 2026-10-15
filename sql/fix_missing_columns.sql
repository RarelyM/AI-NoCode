-- ============================================================
-- 数据库字段同步修复脚本
-- 日期: 2026-10-15
-- 说明: 修复 app 表与实体类 App.java 的字段不同步问题
-- ============================================================

USE `yu_ai_code_mother`;

-- 1. 添加 visual_range 字段（可见范围）
-- 对应实体类: App.java 第 111 行
ALTER TABLE `app`
ADD COLUMN IF NOT EXISTS `visual_range` TINYINT(1) NOT NULL DEFAULT 1
COMMENT '可见范围(默认为1公开,0私有)'
AFTER `updateTime`;

-- 2. 添加 review_message 字段（审核信息）
-- 对应实体类: App.java 第 146 行
ALTER TABLE `app`
ADD COLUMN IF NOT EXISTS `review_message` VARCHAR(512) DEFAULT NULL
COMMENT '审核信息（拒绝原因等）'
AFTER `user_priority`;

-- 3. 验证修复结果
SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'yu_ai_code_mother'
  AND TABLE_NAME = 'app'
  AND COLUMN_NAME IN ('visual_range', 'review_message')
ORDER BY ORDINAL_POSITION;

-- 预期输出:
-- +----------------+------------+--------------------------------+
-- | COLUMN_NAME    | COLUMN_TYPE| COLUMN_COMMENT                 |
-- +----------------+------------+--------------------------------+
-- | visual_range   | tinyint(1) | 可见范围(默认为1公开,0私有)     |
-- | review_message | varchar(512)| 审核信息（拒绝原因等）         |
-- +----------------+------------+--------------------------------+

-- ============================================================
-- 注意事项:
-- 1. 此脚本使用 ADD COLUMN IF NOT EXISTS，可重复执行不会报错
-- 2. 如果数据库版本不支持 IF NOT EXISTS，请去掉该关键字
-- 3. 执行前建议先备份数据库
-- ============================================================