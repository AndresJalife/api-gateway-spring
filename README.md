# api-gateway-spring

## Introduction

This is a sample project to demonstrate how to use Spring Cloud Gateway to build a simple API Gateway.

It helps a project unify the security (authentication and authorization).

The user has to login to get the JWT token. The token is used to access the protected resources.

## Prerequisites
 - Java 17
 - Maven 3.8.1

## Build
 - `mvn clean install`
 - `mvn clean package`
   create table  roles (
   role_id bigint primary key,
   role_name varchar(256) not null
   );

create table users (
user_id bigint primary key,
role_id bigint not null,
name varchar(256) not null,
username varchar(256) not null,
email varchar(256) unique,
password varchar(256) not null,
foreign key (role_id) references roles (role_id)
);

insert into roles(role_id, role_name) values (1, 'ADMIN');
insert into roles(role_id, role_name) values (2, 'BASIC');

insert into users(user_id, role_id, name, username, email, password) values (1, 1, 'Andres Jalife', 'andy', 'andy@gmail.com', 'password');

