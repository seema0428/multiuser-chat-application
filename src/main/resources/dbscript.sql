DROP SCHEMA IF  EXISTS chatapplication;
CREATE SCHEMA chatapplication;
DROP TABLE IF EXISTS chatapplication.users;
CREATE TABLE chatapplication.users(
	id int primary key auto_increment,
	name VARCHAR(30),
	joining_time date);
DROP TABLE IF EXISTS chatapplication.chat_backup;
CREATE TABLE chatapplication.chat_backup(
	msg_id VARCHAR(40) primary key,
	name VARCHAR(30), 
	message VARCHAR(200)
);