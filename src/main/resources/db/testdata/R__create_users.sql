INSERT INTO users (id, first_name, last_name, email, status, deleted, insert_date) VALUES (1, 'admin', '', 'admin@spring-infra.project', 1, FALSE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO credential (id, user_id, username, password, insert_date) VALUES (1, 1, 'admin', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, first_name, last_name, email, status, deleted, insert_date) VALUES (2, 'user', '', 'user@spring-infra.project', 1, FALSE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO credential (id, user_id, username, password, insert_date) VALUES (2, 2, 'user', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO role (id, name, insert_date)
VALUES (1, 'Administration', CURRENT_TIMESTAMP),
       (2, 'Standard User', CURRENT_TIMESTAMP) ON CONFLICT (id) DO NOTHING;

INSERT INTO role_authority (role_id, authority)
SELECT id, 'USER_MANAGEMENT_AUTHORITY'
FROM role
WHERE name = 'Administration'
  AND NOT EXISTS(SELECT 1
                 FROM role_authority
                 where authority = 'USER_MANAGEMENT_AUTHORITY'
                   and role_id IN (SELECT id FROM role WHERE name = 'Administration'));

INSERT INTO role_authority (role_id, authority)
SELECT id, 'MONITORING_AUTHORITY'
FROM role
WHERE name = 'Administration'
  AND NOT EXISTS(SELECT 1
                 FROM role_authority
                 where authority = 'MONITORING_AUTHORITY'
                   and role_id IN (SELECT id FROM role WHERE name = 'Administration'));

INSERT INTO role_authority (role_id, authority)
SELECT id, 'ACCOUNT_INFO_AUTHORITY'
FROM role
WHERE name = 'Administration'
  AND NOT EXISTS(SELECT 1
                 FROM role_authority
                 where authority = 'ACCOUNT_INFO_AUTHORITY'
                   and role_id IN (SELECT id FROM role WHERE name = 'Administration'));

INSERT INTO role_authority (role_id, authority)
SELECT id, 'ACCOUNT_INFO_AUTHORITY'
FROM role
WHERE name = 'Standard User'
  AND NOT EXISTS(SELECT 1
                 FROM role_authority
                 where authority = 'ACCOUNT_INFO_AUTHORITY'
                   and role_id IN (SELECT id FROM role WHERE name = 'Standard User'));

INSERT INTO credential_role (credential_id, role_id, insert_date)
SELECT c.id, r.id, CURRENT_TIMESTAMP
FROM credential c
         INNER JOIN role r ON r.name = 'Administration'
WHERE c.username = 'admin' ON CONFLICT (credential_id, role_id) DO NOTHING;

INSERT INTO credential_role (credential_id, role_id, insert_date)
SELECT c.id, r.id, CURRENT_TIMESTAMP
FROM credential c
         INNER JOIN role r ON r.name = 'Standard User'
WHERE c.username = 'user' ON CONFLICT (credential_id, role_id) DO NOTHING;