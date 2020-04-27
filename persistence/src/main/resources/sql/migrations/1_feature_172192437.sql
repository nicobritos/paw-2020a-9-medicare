create table users
(
    email text not null,
    password text not null,
    users_id serial not null,
    first_name text not null,
    surname text not null,
    phone text
);

create unique index user_email_uindex
    on users (email);

create unique index user_users_id_uindex
    on users (users_id);

alter table users
    add constraint user_pk
        primary key (users_id);

create table patient
(
    user_id int
        constraint patient_users_users_id_fk
            references users
            on update restrict on delete set null,
    office_id int not null
        constraint patient_office_office_id_fk
            references office
            on update restrict on delete cascade,
    patient_id serial not null
);

create unique index patient_patient_id_uindex
    on patient (patient_id);

alter table patient
    add constraint patient_pk
        primary key (patient_id);

alter table office
    add url text;
