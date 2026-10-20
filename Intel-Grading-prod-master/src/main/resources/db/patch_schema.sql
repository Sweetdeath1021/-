-- grade.sql 与当前代码的实体类不一致，导入 grade.sql 后再执行本补丁
ALTER TABLE grd_user CHANGE COLUMN user_phone user_account varchar(50) DEFAULT NULL;
ALTER TABLE grd_region ADD COLUMN region_content text DEFAULT NULL;
ALTER TABLE grd_test_paper ADD COLUMN paper_point text DEFAULT NULL;
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
