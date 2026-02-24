/* Tabela: Users */
CREATE TABLE users (
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

/* Tabela: Orders */
CREATE TABLE orders (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        status VARCHAR(255) NOT NULL,
                        payment_method VARCHAR(100),
                        transaction_id VARCHAR(255),
                        created_at DATETIME(6) NOT NULL,
                        user_id BIGINT NOT NULL,
                        last_updated DATETIME(6) NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;
