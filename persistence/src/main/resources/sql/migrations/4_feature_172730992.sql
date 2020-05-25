create table picture
(
    picture_id serial not null,
    name       text,
    mime_type  text   not null,
    size       bigint not null default 0,
    data       bytea  not null
);

create unique index picture_picture_id_uindex
    on picture (picture_id);

alter table picture
    add constraint picture_pk
        primary key (picture_id);

alter table users
    add profile_id int;

alter table users
    add constraint users_picture_picture_id_fk
        foreign key (profile_id) references picture
            on update cascade on delete set null;
