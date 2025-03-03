INSERT INTO roles (name) VALUES ('${app.test.role}');
INSERT INTO users (id, created_by_id, created_date, last_modified_by_id, last_modified_date, username, email, role_name)
VALUES (1, null, now(), null, now(), '${app.test.user}', '${app.test.email}', '${app.test.role}');
INSERT INTO credentials (id, password) VALUES (1, '{noop}${app.test.password}');
SELECT setval('users_id_seq', max(id)) FROM users;
