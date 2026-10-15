-- 修复 app_version 表中文乱码问题
-- 执行时间: 2026-10-15

-- 设置客户端字符集为 UTF8MB4（关键！）
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE yu_ai_code_mother;

-- 删除旧的乱码表
DROP TABLE IF EXISTS app_version;

-- 重新创建表（确保使用 UTF8MB4 字符集）
CREATE TABLE IF NOT EXISTS app_version (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    appId BIGINT NOT NULL COMMENT '应用id',
    version INT NOT NULL COMMENT '版本号',
    source_code_path VARCHAR(512) COMMENT '源代码路径',
    deploy_key VARCHAR(64) COMMENT '部署标识',
    change_log TEXT COMMENT '版本说明/变更日志',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除',
    INDEX idx_appId (appId),
    INDEX idx_appId_version (appId, version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用版本表';

-- 验证表结构和中文注释是否正确
SHOW CREATE TABLE app_version;

-- 验证字符集
SELECT TABLE_NAME, TABLE_COMMENT, TABLE_COLLATION 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'yu_ai_code_mother' AND TABLE_NAME = 'app_version';