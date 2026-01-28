INSERT IGNORE INTO user (id, first_name, last_name, email, status, deleted, insert_date) VALUE (1, 'admin', '', 'admin@spring-infra.project', 1, 0, NOW());
INSERT IGNORE INTO credential (id, user_id, username, password, insert_date) VALUE (1, 1, 'admin', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', NOW());

INSERT IGNORE INTO user (id, first_name, last_name, email, status, deleted, insert_date) VALUE (2, 'user', '', 'user@spring-infra.project', 1, 0, NOW());
INSERT IGNORE INTO credential (id, user_id, username, password, insert_date) VALUE (2, 2, 'user', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', NOW());

INSERT IGNORE INTO role (id, name, insert_date) VALUES (1, 'Administration', NOW()), (2, 'Standard User', NOW());

INSERT INTO role_authority (role_id, authority) SELECT id, 'USER_MANAGEMENT_AUTHORITY' FROM role WHERE name = 'Administration'
                                                                                                   AND NOT EXISTS(SELECT 1 FROM role_authority
                                                                                                                           where authority = 'USER_MANAGEMENT_AUTHORITY' and role_id IN (SELECT id FROM role WHERE name = 'Administration'));
INSERT INTO role_authority (role_id, authority) SELECT id, 'MONITORING_AUTHORITY' FROM role WHERE name = 'Administration'
                                                                                                   AND NOT EXISTS(SELECT 1 FROM role_authority
                                                                                                                           where authority = 'MONITORING_AUTHORITY' and role_id IN (SELECT id FROM role WHERE name = 'Administration'));
INSERT INTO role_authority (role_id, authority) SELECT id, 'ACCOUNT_INFO_AUTHORITY' FROM role WHERE name = 'Administration'
                                                                                                AND NOT EXISTS(SELECT 1 FROM role_authority
                                                                                                               where authority = 'ACCOUNT_INFO_AUTHORITY' and role_id IN (SELECT id FROM role WHERE name = 'Administration'));
INSERT INTO role_authority (role_id, authority) SELECT id, 'ACCOUNT_INFO_AUTHORITY' FROM role WHERE name = 'Standard User'
                                                                                                AND NOT EXISTS(SELECT 1 FROM role_authority
                                                                                                               where authority = 'ACCOUNT_INFO_AUTHORITY' and role_id IN (SELECT id FROM role WHERE name = 'Standard User'));

INSERT IGNORE INTO credential_role (id, credential_id, role_id, insert_date) select 1, credential.id, role.id, NOW() FROM credential JOIN role on role.name = 'Administration' WHERE username = 'admin';
INSERT IGNORE INTO credential_role (id, credential_id, role_id, insert_date) select 2, credential.id, role.id, NOW() FROM credential JOIN role on role.name = 'Standard User' WHERE username = 'user';