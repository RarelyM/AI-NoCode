-- 修复 app_version 表缺失问题
-- 执行时间: 2026-10-15

USE yu_ai_code_mother;

-- 创建 app_version 表（与代码实体类 @Table("app_version") 对应）
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

-- 验证表是否创建成功
SHOW CREATE TABLE app_version;