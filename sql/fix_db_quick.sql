USE yu_ai_code_mother;

ALTER TABLE app ADD COLUMN visual_range TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'visible range' AFTER updateTime;

ALTER TABLE app ADD COLUMN review_message VARCHAR(512) DEFAULT NULL COMMENT 'review message' AFTER user_priority;