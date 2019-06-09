CREATE DATABASE effugium
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE USER effugium WITH
	LOGIN
	SUPERUSER
	CREATEDB
	CREATEROLE
	INHERIT
	REPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'xxxxxx';    
	
ALTER DATABASE effugium owner TO effugium; 

CREATE SCHEMA effugium AUTHORIZATION effugium; 

CREATE SCHEMA core AUTHORIZATION effugium; 

CREATE SEQUENCE core.sq_database_update START 1 INCREMENT 1; 

CREATE TABLE core.tb_database_update 
  ( 
     id            INT8 NOT NULL, 
     arquivo       VARCHAR(10), 
     atividade     VARCHAR(300), 
     data_inclusao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, 
     executado     BOOLEAN, 
     instancia     VARCHAR(30), 
     linha         INT8, 
     scriptsql     VARCHAR(4000), 
     PRIMARY KEY (id) 
  ); 
