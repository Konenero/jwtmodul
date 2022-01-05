DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL
);

CREATE TABLE users_roles (
    user_id int NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id));

insert into users(user_name, age, username, password)
values ('name1', 25, 'username1', '1234'),
    ('name2', 27, 'username2', '1234'),
    ('name3', 31, 'username3', '1234'),
    ('name4', 20, 'username4', '1234'),
    ('name5', 29, 'username5', '1234');

insert into roles(role_name)
values ('ROLE_USER'), ('ROLE_ADMIN');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (5, 2);