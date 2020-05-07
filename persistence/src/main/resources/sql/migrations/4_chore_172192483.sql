alter table users
    add verified bool default false;

alter table users
    add token text;

create unique index users_token_uindex
    on users (token);
