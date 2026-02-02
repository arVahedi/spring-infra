INSERT INTO users (id, public_id, first_name, last_name, email, status, deleted, created_date)
VALUES (1, '019c20a8-659b-7ae1-a0ed-2141bed430fe', 'admin', '', 'admin@spring-infra.project', 1, FALSE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO credential (id, public_id, user_id, username, password, created_date)
VALUES (1, '019c20aa-1c5d-7711-967f-fcd768665d22', 1, 'admin', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (id, public_id, first_name, last_name, email, status, deleted, created_date)
VALUES (2, '019c20aa-4356-76ea-a9c7-33d3360da31d', 'user', '', 'user@spring-infra.project', 1, FALSE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO credential (id, public_id, user_id, username, password, created_date)
VALUES (2, '019c20aa-7258-738e-ab45-a95271b8bf47', 2, 'user', '$argon2id$v=19$m=4096,t=3,p=1$QnVwdGpUVmR3OUtHV29Pdw$QDBYFDfx9ePp6PwjP95woA', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO role (id, public_id, name, created_date)
VALUES (1, '019c20aa-97c4-77c3-b26e-b4a62aefac46', 'Administration', CURRENT_TIMESTAMP),
       (2, '019c20aa-ad86-7081-ad7c-51b95b1f289e', 'Standard User', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

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

INSERT INTO credential_role (public_id, credential_id, role_id, created_date)
SELECT '019c20ab-64b4-7dea-bff3-02bd3155b2b2', c.id, r.id, CURRENT_TIMESTAMP
FROM credential c
         INNER JOIN role r ON r.name = 'Administration'
WHERE c.username = 'admin'
ON CONFLICT (credential_id, role_id) DO NOTHING;

INSERT INTO credential_role (public_id, credential_id, role_id, created_date)
SELECT '019c20ab-51c6-71ae-a3a0-38eafef65eeb', c.id, r.id, CURRENT_TIMESTAMP
FROM credential c
         INNER JOIN role r ON r.name = 'Standard User'
WHERE c.username = 'user'
ON CONFLICT (credential_id, role_id) DO NOTHING;