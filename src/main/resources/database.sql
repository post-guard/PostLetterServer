use post_letter;

create table if not exists `users`
(
    `id`           int         not null auto_increment,
    `emailAddress` text        not null,
    `nickname`     text        not null,
    `password`     varchar(64) not null,
    primary key (`id`)
);

create table if not exists `groups`
(
    `id`      int         not null auto_increment,
    `name`    varchar(20) not null,
    `details` text,
    primary key (`id`)
);

create table if not exists `user_group_links`
(
    `id`         int not null auto_increment,
    `userId`     int not null,
    `groupId`    int not null,
    `permission` int not null,
    primary key (`id`)
);