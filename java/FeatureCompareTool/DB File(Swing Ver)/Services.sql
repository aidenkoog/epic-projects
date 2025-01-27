DROP DATABASE IF EXISTS Services;

CREATE DATABASE Services;

USE services;

CREATE TABLE oldservices
(
	SName char(30) not null,
	UDName char(30) not null,
	Task char(30) not null,
	SSName char(30) not null,
	InterfaceName char(50) not null,
	Component char(30) not null,
	Definition char(80) not null,
	ServiceID char(20) not null,
	RegAuthority char(50) not null
);