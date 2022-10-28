
Kubernetes requests
POST http://34.73.35.156:80/ms/genrator/generate/123
GET http://34.73.35.156:80/ms/genrator/archives/123

postgresSQL
To migrate existing data from a previous major version of PostgreSQL run:
  brew postgresql-upgrade-database

This formula has created a default database cluster with:
  initdb --locale=C -E UTF-8 /usr/local/var/postgres
For more details, read:
  https://www.postgresql.org/docs/13/app-initdb.html

To have launchd start postgresql now and restart at login:
  brew services start postgresql
Or, if you don't want/need a background service you can just run:
  pg_ctl -D /usr/local/var/postgres start
  pg_ctl -D /usr/local/var/postgres stop 
  
psql postgres

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
mysqlroot

mysql -uroot -p

create database custgendb;

CREATE USER 'custdbuser1'@'localhost' IDENTIFIED BY 'ChangeMe';
GRANT ALL PRIVILEGES ON custgendb.*  TO 'custdbuser1'@'localhost';

use custgendb;

create table customer
(
    id int primary key auto_increment,
    first_name varchar(30),
    last_name varchar(30)
);

create table address
(
    customer integer primary key references customer(id),
    address1 varchar(30),
    address2 varchar(30),
    city varchar(30),
    state varchar(30),
    zipcode varchar(30),
    country varchar(30)
);

create table email
(
    id int primary key auto_increment,
    customer int  references customer(id),
    customer_key int,
    emailaddress varchar(30),
    emailtype varchar(30)
);
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

