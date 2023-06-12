# api-gateway-spring

## Introduction

This is a sample project to demonstrate how to use Spring Cloud Gateway to build a simple API Gateway.

It helps a project unify the security (authentication and authorization) and could evolve into a load-balancer and monitor of services.

## Prerequisites
 - Java 17
 - Maven 3.8.1
 - Postgresql

## Build
 - `mvn clean install`
 - `mvn clean package`


### Database Tables Needed (Postgres)
```sql
create table  roles (
      role_id bigint primary key,
      role_name varchar(256) not null
   );
```
```sql
   create table users (
      user_id bigint primary key,
      role_id bigint not null,
      name varchar(256) not null,
      username varchar(256) not null,
      email varchar(256) unique,
      password varchar(256) not null,
      foreign key (role_id) references roles (role_id)
   );
  ```
### Way of use
- Create a user from the 'user' endpoints
- Login and obtain the acces and refresh token from the 'login' endpoints
- When the access token is about to expire, use the refresh token to obtain a new access token from the 'refresh' endpoints 