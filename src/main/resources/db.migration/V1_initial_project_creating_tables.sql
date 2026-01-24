/* Tabela: Users */
CREATE TABLE Users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_premium BIT(1) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    last_updated DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;

/* Tabela: UserSettings */
CREATE TABLE UserSettings (
  id BIGINT NOT NULL AUTO_INCREMENT,
  theme VARCHAR(50),
  font_size INT,
  font_family VARCHAR(100),
  user_id BIGINT NOT NULL,
  date_created DATETIME(6) NOT NULL,
  last_updated DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_usersettings_user FOREIGN KEY (user_id) REFERENCES Users (id)
) ENGINE=InnoDB;

/* Tabela: Books */
CREATE TABLE Books (
   id BIGINT NOT NULL AUTO_INCREMENT,
   title VARCHAR(255) NOT NULL,
   author VARCHAR(255) NOT NULL,
   description LONGTEXT,
   cover_url LONGTEXT,
   file_url LONGTEXT NOT NULL,
   file_name VARCHAR(255) NOT NULL,
   file_type VARCHAR(255) NOT NULL,
   is_premium TINYINT(1) NOT NULL,
   uploaded_at DATETIME(6),
   user_id BIGINT NOT NULL,
   date_created DATETIME(6) NOT NULL,
   last_updated DATETIME(6) NOT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_books_user FOREIGN KEY (user_id) REFERENCES Users (id)
) ENGINE=InnoDB;

/* Tabela: Orders */
CREATE TABLE Orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(100),
    transaction_id VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    user_id BIGINT NOT NULL,
    last_updated DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES Users (id)
) ENGINE=InnoDB;

/* Tabela: ReadingProgresses */
CREATE TABLE ReadingProgresses (
   id BIGINT NOT NULL AUTO_INCREMENT,
   current_page INT,
   percentage_completed DOUBLE,
   last_cfi_range VARCHAR(255),
   device_type VARCHAR(100),
   last_read_at DATETIME(6),
   user_id BIGINT NOT NULL,
   book_id BIGINT NOT NULL,
   date_created DATETIME(6) NOT NULL,
   last_updated DATETIME(6) NOT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_readingprogress_user FOREIGN KEY (user_id) REFERENCES Users (id),
   CONSTRAINT fk_readingprogress_book FOREIGN KEY (book_id) REFERENCES Books (id)
) ENGINE=InnoDB;