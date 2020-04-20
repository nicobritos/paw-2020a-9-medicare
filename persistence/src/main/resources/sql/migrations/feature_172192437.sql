create table "user"
(
    email text not null,
    password text not null,
    user_id serial not null,
    first_name text not null,
    surname text not null,
    phone text
);

create unique index user_email_uindex
    on "user" (email);

create unique index user_user_id_uindex
    on "user" (user_id);

alter table "user"
    add constraint user_pk
        primary key (user_id);

create table patient
(
    user_id int
        constraint patient_user_user_id_fk
            references "user"
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
