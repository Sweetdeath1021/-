-- grade.sql 与当前代码的实体类不一致，导入 grade.sql 后再执行本补丁（可重复执行）
DROP PROCEDURE IF EXISTS grd_patch_exec_if;
DELIMITER //
CREATE PROCEDURE grd_patch_exec_if(cond INT, stmt TEXT)
BEGIN
  IF cond > 0 THEN
    SET @grd_patch_sql = stmt;
    PREPARE s FROM @grd_patch_sql;
    EXECUTE s;
    DEALLOCATE PREPARE s;
  END IF;
END //
DELIMITER ;

CALL grd_patch_exec_if(
  (SELECT COUNT(*) FROM information_schema.columns
   WHERE table_schema = DATABASE() AND table_name = 'grd_user' AND column_name = 'user_phone'),
  'ALTER TABLE grd_user CHANGE COLUMN user_phone user_account varchar(50) DEFAULT NULL');
CALL grd_patch_exec_if(
  1 - (SELECT COUNT(*) FROM information_schema.columns
   WHERE table_schema = DATABASE() AND table_name = 'grd_region' AND column_name = 'region_content'),
  'ALTER TABLE grd_region ADD COLUMN region_content text DEFAULT NULL');
CALL grd_patch_exec_if(
  1 - (SELECT COUNT(*) FROM information_schema.columns
   WHERE table_schema = DATABASE() AND table_name = 'grd_test_paper' AND column_name = 'paper_point'),
  'ALTER TABLE grd_test_paper ADD COLUMN paper_point text DEFAULT NULL');

DROP PROCEDURE IF EXISTS grd_patch_exec_if;

CREATE TABLE IF NOT EXISTS grd_comment (
  comment_id varchar(50) NOT NULL,
  user_id varchar(50) DEFAULT NULL,
  comment_name varchar(255) DEFAULT NULL,
  comment_content text DEFAULT NULL,
  comment_subject varchar(100) DEFAULT NULL,
  PRIMARY KEY (comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS grd_question (
  user_id varchar(50) DEFAULT NULL,
  paper_id varchar(50) DEFAULT NULL,
  question_content text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
