create table user
(
    id       bigint auto_increment
        primary key,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) not null
);

create table addresses
(
    id      bigint auto_increment
        primary key,
    street  varchar(255) not null,
    city    varchar(255) not null,
    zip     varchar(255) not null,
    user_id bigint       not null,
    constraint addresses_user_id_fk
        foreign key (user_id) references `user` (id)
);

create table profiles
(
    id             bigint auto_increment primary key ,
    bio            varchar(25) null,
    phone_number   int         null,
    date_of_birth  date        null,
    loyalty_points int unsigned     default 0,
    foreign key (id) references user(id) on delete cascade
);

