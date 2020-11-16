-- Drop table
drop table if exists "NOTE";

-- Create table
create table "NOTE" (
    id bigserial not null,
    title varchar(200) not null,
    description varchar(2048) not null,
    location varchar(100),
    event_date timestamp,
    create_date timestamp,
    primary key (id)
);