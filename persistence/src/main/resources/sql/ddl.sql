create table system_country
(
    country_id varchar(2) default NULL::character varying not null
        constraint system_country_pk
            primary key,
    name text not null
);

create unique index system_country_country_id_uindex
    on system_country (country_id);

create table system_province
(
    province_id serial not null
        constraint system_province_pk
            primary key,
    country_id varchar(2)
        constraint system_province_country
            references system_country
            on update restrict on delete cascade,
    name text not null
);

create unique index system_province_province_id_uindex
    on system_province (province_id);

create table system_staff_specialty
(
    specialty_id serial not null
        constraint specialty_pk
            primary key,
    name text not null
);

create unique index specialty_specialty_id_uindex
    on system_staff_specialty (specialty_id);

create table system_locality
(
    province_id integer
        constraint system_locality_province
            references system_province
            on update restrict on delete restrict,
    name text not null,
    locality_id serial not null
        constraint system_locality_pk
            primary key
);

create table office
(
    office_id serial not null
        constraint office_pk
            primary key,
    name text not null,
    street text,
    locality_id integer not null
        constraint office_province_id
            references system_locality
            on update restrict on delete restrict,
    phone text,
    email text
);

create unique index office_office_id_uindex
    on office (office_id);

create table staff
(
    staff_id serial not null
        constraint staff_pk
            primary key,
    office_id integer
        constraint staff_office
            references office
            on update restrict on delete cascade,
    first_name text not null,
    surname text not null,
    phone text,
    email text,
    registration_number integer
);

create unique index staff_staff_id_uindex
    on staff (staff_id);

create table system_staff_specialty_staff
(
    specialty_id integer not null
        constraint specialty_staff_system_specialty
            references system_staff_specialty
            on update restrict on delete restrict,
    staff_id integer not null
        constraint specialty_staff_staff
            references staff
            on update restrict on delete cascade,
    constraint system_staff_specialty_staff_pk
        primary key (specialty_id, staff_id)
);

create unique index system_locality_locality_id_uindex
    on system_locality (locality_id);

