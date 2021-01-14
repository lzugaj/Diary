-- Drop table
drop table if exists "USER" cascade;
drop table if exists "NOTE" cascade;

-- Create table
create table "USER" (
    id bigserial not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    username varchar(20) not null,
    password varchar(20) not null,
    email varchar(100) not null,
    number_of_notes int,
    is_active boolean,
    primary key (id)
);

create table "NOTE" (
    id bigserial not null,
    title varchar(200) not null,
    description varchar(2048) not null,
    location varchar(100),
    event_date date,
    creation_date timestamp,
    user_id bigint not null,
    primary key (id),
    constraint note_fk foreign key (user_id) references "USER" (id)
);