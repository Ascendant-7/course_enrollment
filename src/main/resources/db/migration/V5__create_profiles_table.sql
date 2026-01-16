CREATE TABLE profiles (
    account_id BIGINT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bio TEXT,
    phone VARCHAR(20),
    address VARCHAR(255),
    CONSTRAINT fk_profile_account FOREIGN KEY (account_id) REFERENCES accounts(id)
)