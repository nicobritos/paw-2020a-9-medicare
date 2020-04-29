create table appointment
(
    appointment_id serial not null
        constraint appointment_pk
            primary key,
    status text not null,
    patient_id integer not null
        constraint appointment_patient_patient_id_fk
            references patient
            on update restrict on delete restrict,
    staff_id integer not null,
    from_date date not null,
    to_date date not null
);

alter table appointment owner to postgres;

create unique index appointment_appointment_id_uindex
    on appointment (appointment_id);

create index appointment_from_date_to_date_index
    on appointment (from_date, to_date);

create index appointment_status_status_index
    on appointment (status, status);

create table workday
(
    workday_id serial not null,
    staff_id int not null
        constraint workday_staff_staff_id_fk
            references staff
            on update restrict on delete cascade,
    start_hour int not null,
    end_hour int not null,
    start_minute int not null default 0,
    end_minute int not null default 0,
    day text not null
);

create index workday_day_index
    on workday (day);

create unique index workday_workday_id_uindex
    on workday (workday_id);

alter table workday
    add constraint workday_pk
        primary key (workday_id);
