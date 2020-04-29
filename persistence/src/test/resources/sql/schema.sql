create table system_country
(
    country_id varchar(2) not null
        constraint system_country_pk
        primary key,
    name varchar(255) not null
);

create table system_province
(
    province_id identity not null
        constraint system_province_pk
            primary key,
    country_id varchar(2)
        constraint system_province_country
            references system_country
            ,
    name varchar(255) not null
);

create table system_staff_specialty
(
    specialty_id identity not null
        constraint specialty_pk
            primary key,
    name varchar(255) not null
);

create table system_locality
(
    province_id integer
        constraint system_locality_province
            references system_province
            ,
    name varchar(255) not null,
    locality_id identity not null
        constraint system_locality_pk
            primary key
);

create table office
(
    office_id identity not null
        constraint office_pk
            primary key,
    name varchar(255) not null,
    street varchar(255),
    locality_id integer
        constraint office_province_id
            references system_locality
            ,
    phone varchar(255),
    email varchar(255),
    url varchar(255)
);

create table users
(
    email varchar(255) not null,
    password varchar(255) not null,
    users_id identity not null
        constraint user_pk
            primary key,
    first_name varchar(255) not null,
    surname varchar(255) not null,
    phone varchar(255)
);

create table staff
(
    staff_id identity not null
        constraint staff_pk
            primary key,
    office_id integer
        constraint staff_office
            references office
            ,
    first_name varchar(255) not null,
    surname varchar(255) not null,
    phone varchar(255),
    email varchar(255),
    registration_number integer,
    user_id integer
        constraint staff_users_users_id_fk
            references users
);

create table system_staff_specialty_staff
(
    specialty_id integer not null
        constraint specialty_staff_system_specialty
            references system_staff_specialty
            ,
    staff_id integer not null
        constraint specialty_staff_staff
            references staff
            ,
    constraint system_staff_specialty_staff_pk
        primary key (specialty_id, staff_id)
);

create table patient
(
    user_id integer
        constraint patient_user_user_id_fk
            references users,
    office_id integer not null
        constraint patient_office_office_id_fk
            references office
            ,
    patient_id identity not null
        constraint patient_pk
            primary key
);

create table appointment
(
    appointment_id identity not null
        constraint appointment_pk
            primary key,
    status varchar(255) not null,
    patient_id integer not null
        constraint appointment_patient_patient_id_fk
            references patient
            ,
    staff_id integer not null,
    from_date date not null,
    to_date date not null
);

create table workday
(
    workday_id identity not null
        constraint workday_pk
            primary key,
    staff_id integer
        constraint workday_staff_staff_id_fk
            references staff,
    start_hour integer not null,
    end_hour integer not null,
    day varchar(255) not null,
    start_minute integer default 0 not null,
    end_minute integer default 0 not null
);
