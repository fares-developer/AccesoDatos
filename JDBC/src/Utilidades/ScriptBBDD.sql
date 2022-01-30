create database biblioteca;
use biblioteca;

create table libros (
					lib_isbn varchar(24) not null,
                    lib_titulo varchar(100) not null,
                    lib_autor varchar(100) not null,
                    lib_paginas int unsigned,
                    lib_edicion year not null,
                    primary key LIB_PK(lib_isbn)
);

create table socios (
					soc_dni varchar(16) not null,
                    soc_nombre varchar(90) not null,
                    primary key SOC_PK(soc_dni)
);

create table prestamos (
						pre_fecini datetime not null,
                        pre_fecfin datetime,
                        pre_isbn varchar(24) not null,
                        pre_dni varchar(16) not null,
                        primary key PRE_PK(pre_fecini,pre_isbn,pre_dni)
);

alter table prestamos add foreign key PRE_LIB_FK(pre_isbn) references libros(lib_isbn);
alter table prestamos add foreign key PRE_SOC_FK(pre_dni) references socios(soc_dni);