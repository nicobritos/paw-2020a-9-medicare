alter table users
    add verified bool default false;

alter table users
    add token text;

