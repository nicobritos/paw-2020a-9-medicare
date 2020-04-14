create table if not exists system_country
(
    country_id varchar(2) default NULL
        constraint system_country_pk
            primary key,
    name varchar(255) not null
);

create table if not exists system_province
(
    province_id identity not null
        constraint system_province_pk
            primary key,
    country_id varchar(2)
        constraint system_province_country
            references system_country
--             on update restrict on delete cascade
,
    name varchar(255) not null
);

create table system_locality
(
    province_id integer
        constraint system_locality_province
            references system_province
--             on update restrict on delete restrict,
        ,
    name varchar(255) not null,
    locality_id identity not null
        constraint system_locality_pk
            primary key
);


create table if not exists office
(
    office_id identity not null
        constraint office_pk
            primary key,
    name varchar(255) not null,
    street varchar(255),
    province_id integer
        constraint office_province_id
            references system_province
--             on update restrict on delete restrict
,
    phone varchar(255),
    email varchar(255),
    street_number integer not null
);

create table if not exists staff
(
    staff_id identity not null
        constraint staff_pk
            primary key,
    office_id integer
        constraint staff_office
            references office
--             on update restrict on delete cascade
,
    first_name varchar(255) not null,
    surname varchar(255) not null,
    phone varchar(255),
    email varchar(255),
    registration_number integer not null
);

create table if not exists system_staff_specialty
(
    specialty_id identity not null
        constraint specialty_pk
            primary key,
    name varchar(255) not null
);

create table if not exists system_staff_specialty_staff
(
    specialty_id integer not null
        constraint specialty_staff_system_specialty
            references system_staff_specialty
--             on update restrict on delete restrict
,
    staff_id integer not null
        constraint specialty_staff_staff
            references staff
--             on update restrict on delete cascade
);