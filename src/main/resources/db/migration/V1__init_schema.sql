CREATE SEQUENCE credential_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
CREATE TABLE credential
(
    id                 BIGINT PRIMARY KEY DEFAULT nextval('credential_id_seq'),
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    password           VARCHAR(255),
    username           VARCHAR(255),
    user_id            BIGINT
);
ALTER SEQUENCE credential_id_seq OWNED BY credential.id;

CREATE SEQUENCE credential_role_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
CREATE TABLE credential_role
(
    id                 BIGINT PRIMARY KEY DEFAULT nextval('credential_role_id_seq'),
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    credential_id      BIGINT,
    role_id            INTEGER
);
ALTER SEQUENCE credential_role_id_seq OWNED BY credential_role.id;


CREATE TABLE credential_role_audit_log
(
    id            INTEGER NOT NULL,
    rev           INTEGER NOT NULL,
    revtype       SMALLINT,
    credential_id BIGINT,
    role_id       INTEGER,
    PRIMARY KEY (id, rev)
);

CREATE SEQUENCE revision_info_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
CREATE TABLE revision_info
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('revision_info_id_seq'),
    occurrence_date TIMESTAMP,
    principal       VARCHAR(255)
);
ALTER SEQUENCE revision_info_id_seq OWNED BY revision_info.id;

CREATE SEQUENCE role_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
CREATE TABLE role
(
    id                 BIGINT PRIMARY KEY DEFAULT nextval('role_id_seq'),
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    name               VARCHAR(255)
);
ALTER SEQUENCE role_id_seq OWNED BY role.id;

CREATE TABLE role_audit_log
(
    id      INTEGER NOT NULL,
    rev     INTEGER NOT NULL,
    revtype SMALLINT,
    name    VARCHAR(255),
    PRIMARY KEY (id, rev)
);

CREATE TABLE role_authority
(
    role_id   INTEGER NOT NULL,
    authority VARCHAR(255),
    PRIMARY KEY (role_id, authority)
);

CREATE TABLE role_authority_audit_log
(
    rev       INTEGER      NOT NULL,
    role_id   INTEGER      NOT NULL,
    authority VARCHAR(255) NOT NULL,
    revtype   SMALLINT,
    PRIMARY KEY (rev, role_id, authority)
);

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;
CREATE TABLE users
(
    id                 BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    deleted            boolean,
    email              VARCHAR(255),
    first_name         VARCHAR(255),
    last_name          VARCHAR(255),
    phone              VARCHAR(255),
    status             INTEGER
);
ALTER SEQUENCE users_id_seq OWNED BY users.id;

CREATE TABLE users_audit_log
(
    id         BIGINT  NOT NULL,
    rev        INTEGER NOT NULL,
    revtype    SMALLINT,
    deleted    boolean,
    email      VARCHAR(255),
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    phone      VARCHAR(255),
    status     INTEGER,
    PRIMARY KEY (id, rev)
);

ALTER TABLE credential_role
    ADD CONSTRAINT unique_credential_role UNIQUE (credential_id, role_id);

ALTER TABLE credential
    ADD CONSTRAINT FKpg7bdnqxpyhrt7f8soul9y7ne FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE credential_role
    ADD CONSTRAINT FKhtbtll4hfbsdcw3y6u7a42cr0 FOREIGN KEY (credential_id) REFERENCES credential (id);
ALTER TABLE credential_role
    ADD CONSTRAINT FKq04pu4sw323gkv0s865lvgrrg FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE credential_role_audit_log
    ADD CONSTRAINT FKi8j7mh445cdde7lqkgndwa1fb FOREIGN KEY (rev) REFERENCES revision_info (id);
ALTER TABLE role_audit_log
    ADD CONSTRAINT FKnws60t3juhebdgd282455e9d9 FOREIGN KEY (rev) REFERENCES revision_info (id);
ALTER TABLE role_authority
    ADD CONSTRAINT FK2052966dco7y9f97s1a824bj1 FOREIGN KEY (role_id) REFERENCES role (id);
ALTER TABLE role_authority_audit_log
    ADD CONSTRAINT FK779y5rfjpjkti53ndh4hfqykd FOREIGN KEY (rev) REFERENCES revision_info (id);
ALTER TABLE users_audit_log
    ADD CONSTRAINT FK653p67p17jotfw2p6xsuee8bp FOREIGN KEY (rev) REFERENCES revision_info (id);
