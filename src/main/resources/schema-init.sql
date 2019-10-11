-- MySQL init table SQL

CREATE TABLE IF NOT EXISTS saga_event(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  saga_id VARCHAR(255) null,
  type VARCHAR(255) null,
  content_json TEXT null,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX saga_id_index(saga_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8