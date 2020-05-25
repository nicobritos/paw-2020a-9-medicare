alter table appointment
    add constraint appointment_staff_staff_id_fk
        foreign key (staff_id) references staff
            on update set null on delete set null;

alter table appointment
    alter column from_date type timestamp using from_date::timestamp;
