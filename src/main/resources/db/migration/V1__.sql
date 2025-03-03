CREATE TABLE clients
(
    id                  SERIAL                      NOT NULL,
    created_by_id       BIGINT,
    created_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_by_id BIGINT,
    last_modified_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    first_name          VARCHAR(64)                 NOT NULL,
    last_name           VARCHAR(128)                NOT NULL,
    patronymic          VARCHAR(64),
    email               VARCHAR(320),
    phone               VARCHAR(30),
    CONSTRAINT pk_clients PRIMARY KEY (id)
);

CREATE TABLE credentials
(
    id       BIGINT       NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_credentials PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens
(
    token       UUID                        NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     BIGINT                      NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (token)
);

CREATE TABLE role_authorities
(
    role_name VARCHAR(32) NOT NULL,
    authority VARCHAR(32) NOT NULL
);

CREATE TABLE roles
(
    name VARCHAR(32) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (name)
);

CREATE TABLE users
(
    id                  SERIAL                      NOT NULL,
    created_by_id       BIGINT,
    created_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_by_id BIGINT,
    last_modified_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    username            VARCHAR(32)                 NOT NULL,
    email               VARCHAR(320)                NOT NULL,
    role_name           VARCHAR(32)                 NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_CREATEDBY FOREIGN KEY (created_by_id) REFERENCES users (id);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_LASTMODIFIEDBY FOREIGN KEY (last_modified_by_id) REFERENCES users (id);

ALTER TABLE credentials
    ADD CONSTRAINT FK_CREDENTIALS_ON_ID FOREIGN KEY (id) REFERENCES users (id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CREATEDBY FOREIGN KEY (created_by_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_LASTMODIFIEDBY FOREIGN KEY (last_modified_by_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE_NAME FOREIGN KEY (role_name) REFERENCES roles (name);

ALTER TABLE role_authorities
    ADD CONSTRAINT fk_role_authorities_on_role FOREIGN KEY (role_name) REFERENCES roles (name);