drop table if exists `users`;
create table if not exists `users`
(
    `id`           int         not null auto_increment,
    `emailAddress` text        not null,
    `nickname`     text        not null,
    `password`     varchar(64) not null,
    primary key (`id`)
);

drop table if exists `sessions`;
create table if not exists `sessions`
(
    `id` int not null  auto_increment,
    `name` text not null ,
    `details` text not null ,
    `level` int not null ,
    primary key (`id`)
);

drop table if exists `participants`;
create table if not exists `participants`
(
    `id` int not null auto_increment,
    `userId` int not null ,
    `sessionId` int not null ,
    `permission` int not null ,
    primary key (`id`)
);

