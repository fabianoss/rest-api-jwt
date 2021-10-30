create sequence seq_role start with 1 increment by  1;
create sequence seq_users start with 1 increment by  1;

    create table roles (
       id number(38) not null,
        name varchar2(20 char),
        primary key (id)
    );

    create table user_roles (
       user_id number(38) not null,
        role_id number(38) not null,
        primary key (user_id, role_id)
    );

    create table users (
       id number(38) not null,
        email varchar2(50 char),
        password varchar2(120 char),
        username varchar2(20 char),
        primary key (id)
    );

    alter table users 
       add constraint UKr43af9ap4edm43mmtq01oddj6 unique (username);

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table user_roles 
       add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 
       foreign key (role_id) 
       references roles;

    alter table user_roles 
       add constraint FKhfh9dx7w3ubf1co1vdev94g3f 
       foreign key (user_id) 
       references users;
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
