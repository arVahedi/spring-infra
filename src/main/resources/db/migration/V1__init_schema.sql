create table credential
(
    id                 bigint not null auto_increment,
    insert_date        datetime,
    last_modified_date datetime,
    password           varchar(255),
    username           varchar(255),
    user_id            bigint,
    primary key (id)
) engine = InnoDB;

create table credential_role
(
    id                 integer not null auto_increment,
    insert_date        datetime,
    last_modified_date datetime,
    credential_id      bigint,
    role_id            integer,
    primary key (id)
) engine = InnoDB;

create table credential_role_audit_log
(
    id            integer not null,
    rev           integer not null,
    revtype       tinyint,
    credential_id bigint,
    role_id       integer,
    primary key (id, rev)
) engine = InnoDB;

create table revision_info
(
    id              integer not null auto_increment,
    occurrence_date datetime,
    principal       varchar(255),
    primary key (id)
) engine = InnoDB;

create table role
(
    id                 integer not null auto_increment,
    insert_date        datetime,
    last_modified_date datetime,
    name               varchar(255),
    primary key (id)
) engine = InnoDB;

create table role_audit_log
(
    id      integer not null,
    rev     integer not null,
    revtype tinyint,
    name    varchar(255),
    primary key (id, rev)
) engine = InnoDB;

create table role_authority
(
    role_id   integer not null,
    authority varchar(255)
) engine = InnoDB;

create table role_authority_audit_log
(
    rev       integer      not null,
    role_id   integer      not null,
    authority varchar(255) not null,
    revtype   tinyint,
    primary key (rev, role_id, authority)
) engine = InnoDB;

create table users
(
    id                 bigint not null auto_increment,
    insert_date        datetime,
    last_modified_date datetime,
    deleted            bit,
    email              varchar(255),
    first_name         varchar(255),
    last_name          varchar(255),
    phone              varchar(255),
    status             integer,
    primary key (id)
) engine = InnoDB;

create table users_audit_log
(
    id         bigint  not null,
    rev        integer not null,
    revtype    tinyint,
    deleted    bit,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    phone      varchar(255),
    status     integer,
    primary key (id, rev)
) engine = InnoDB;

alter table credential
    add constraint FKpg7bdnqxpyhrt7f8soul9y7ne foreign key (user_id) references users (id);
alter table credential_role
    add constraint FKhtbtll4hfbsdcw3y6u7a42cr0 foreign key (credential_id) references credential (id);
alter table credential_role
    add constraint FKq04pu4sw323gkv0s865lvgrrg foreign key (role_id) references role (id);
alter table credential_role_audit_log
    add constraint FKi8j7mh445cdde7lqkgndwa1fb foreign key (rev) references revision_info (id);
alter table role_audit_log
    add constraint FKnws60t3juhebdgd282455e9d9 foreign key (rev) references revision_info (id);
alter table role_authority
    add constraint FK2052966dco7y9f97s1a824bj1 foreign key (role_id) references role (id);
alter table role_authority_audit_log
    add constraint FK779y5rfjpjkti53ndh4hfqykd foreign key (rev) references revision_info (id);
alter table users_audit_log
    add constraint FK653p67p17jotfw2p6xsuee8bp foreign key (rev) references revision_info (id);
