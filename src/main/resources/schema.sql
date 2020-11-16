-- Drop table
drop table if exists "NOTE" cascade;
drop table if exists "USER" cascade;

-- Create table
create table "NOTE" (
    id bigserial not null,
    title varchar(200) not null,
    description varchar(2048) not null,
    location varchar(100),
    event_date timestamp,
    creation_date timestamp,
    primary key (id)
);

create table "USER" (
    id bigserial not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    username varchar(20) not null,
    password varchar(20) not null,
    email varchar(100) not null,
    city varchar(20),
    country varchar(20),
    number_of_nNotes int,
    is_active boolean,
    note_id bigint not null,
    primary key (id),
    constraint note_fk foreign key (note_id) references "NOTE" (id)
);