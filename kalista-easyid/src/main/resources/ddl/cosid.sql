-- auto-generated definition
create table cosid
(
    name            varchar(100)     not null
        primary key,
    last_max_id     bigint default 0 not null,
    last_fetch_time bigint           not null
);

-- auto-generated definition
create table cosid_machine
(
    name            varchar(100)                               not null
        primary key,
    namespace       varchar(100)                               not null,
    machine_id      integer      default 0                     not null,
    last_timestamp  bigint       default 0                     not null,
    instance_id     varchar(100) default ''::character varying not null,
    distribute_time bigint       default 0                     not null,
    revert_time     bigint       default 0                     not null
);

alter table cosid_machine
    owner to postgres;

create index idx_instance_id
    on cosid_machine (instance_id);

create index idx_namespace
    on cosid_machine (namespace);