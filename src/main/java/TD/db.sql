create database mini_football_db;

create user mini_football_db_manager with password '123456';

\c mini_football_db;

grant connect on database mini_football_db to mini_football_db_manager;

grant create on schema public to mini_football_db_manager;