create sequence sequence_access_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_account_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_car_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_confirmation_code_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_course_details_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_course_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_driving_lesson_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_lecture_group_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_lecture_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create sequence sequence_payment_id
    as bigint
    increment 1
    start 1
    minvalue 1
    maxvalue 9223372036854775807;

create table account
(
    id                         bigint       not null,
    enabled                    boolean      not null,
    confirmed                  boolean      not null,
    login                      varchar(20)  not null,
    email_address              varchar(127) not null,
    new_email_address          varchar(127),
    password                   varchar(64)  not null,
    firstname                  varchar(31)  not null,
    lastname                   varchar(31)  not null,
    language                   varchar(2)   not null,
    phone_number               varchar(15),
    failed_login_attempts      integer default 0,
    enable_modification_date   timestamp,
    enable_modification_by     bigint,
    confirm_modification_date  timestamp,
    confirm_modification_by    bigint,
    email_modification_date    timestamp,
    email_modification_by      bigint,
    password_modification_date timestamp,
    password_modification_by   bigint,
    version                    bigint       not null,
    creation_date              timestamp    not null,
    created_by                 bigint       not null,
    modification_date          timestamp,
    modified_by                bigint,
    constraint pk_account_id
        primary key (id),
    constraint constraint_account_login
        unique (login),
    constraint constraint_account_email_address
        unique (email_address),
    constraint constraint_account_phone_number
        unique (phone_number),
    constraint fk_account_enable_modification_by
        foreign key (enable_modification_by) references account,
    constraint fk_account_confirm_modification_by
        foreign key (confirm_modification_by) references account,
    constraint fk_account_email_modification_by
        foreign key (email_modification_by) references account,
    constraint fk_account_password_modification_by
        foreign key (password_modification_by) references account,
    constraint fk_account_created_by
        foreign key (created_by) references account,
    constraint fk_account_modified_by
        foreign key (modified_by) references account,
    constraint account_failed_login_attempts_check
        check (failed_login_attempts >= 0)
);

create table access
(
    id                bigint      not null,
    access_type       varchar(31) not null,
    activated         boolean     not null,
    account_id        bigint      not null,
    version           bigint      not null,
    creation_date     timestamp   not null,
    created_by        bigint      not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_access_id
        primary key (id),
    constraint constraint_access_access_type_account_id
        unique (access_type, account_id),
    constraint fk_access_account_id
        foreign key (account_id) references account,
    constraint fk_access_created_by
        foreign key (created_by) references account,
    constraint fk_access_modified_by
        foreign key (modified_by) references account
);

create table admin_access
(
    id bigint not null,
    constraint pk_admin_access_id
        primary key (id),
    constraint fk_admin_access_access_id
        foreign key (id) references access
);

create table instructor_access
(
    id bigint not null,
    constraint pk_instructor_access_id
        primary key (id),
    constraint fk_instructor_access_access_id
        foreign key (id) references access
);

create table trainee_access
(
    id bigint not null,
    constraint pk_trainee_access_id
        primary key (id),
    constraint fk_trainee_access_access_id
        foreign key (id) references access
);

create table confirmation_code
(
    id                bigint       not null,
    code              varchar(128) not null,
    used              boolean      not null,
    send_attempts     integer default 0,
    account_id        bigint       not null,
    code_type         integer      not null,
    version           bigint       not null,
    creation_date     timestamp    not null,
    created_by        bigint       not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_confirmation_code_id
        primary key (id),
    constraint constraint_confirmation_code_code
        unique (code),
    constraint fk_confirmation_code_account_id
        foreign key (account_id) references account,
    constraint fk_confirmation_code_created_by
        foreign key (created_by) references account,
    constraint fk_confirmation_code_modified_by
        foreign key (modified_by) references account
);

create table car
(
    id                  bigint      not null,
    course_category     varchar(1)  not null,
    image               varchar(31),
    brand               varchar(31) not null,
    model               varchar(31) not null,
    registration_number varchar(7)  not null,
    production_year     integer     not null,
    deleted             boolean     not null,
    version             bigint      not null,
    creation_date       timestamp   not null,
    created_by          bigint      not null,
    modification_date   timestamp,
    modified_by         bigint,
    constraint pk_car_id
        primary key (id),
    constraint fk_car_created_by
        foreign key (created_by) references account,
    constraint fk_car_modified_by
        foreign key (modified_by) references account,
    constraint car_production_year_check
        check ((production_year >= 2005) AND (production_year <= 2022))
);

create table course_details
(
    id                bigint     not null,
    course_category   varchar(1) not null,
    price             numeric(4) not null,
    lectures_hours    integer    not null,
    driving_hours     integer    not null,
    version           bigint     not null,
    creation_date     timestamp  not null,
    created_by        bigint     not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_course_details_id
        primary key (id),
    constraint constraint_course_details_course_category
        unique (course_category),
    constraint fk_course_details_created_by
        foreign key (created_by) references account,
    constraint fk_course_details_modified_by
        foreign key (modified_by) references account,
    constraint course_details_driving_hours_check
        check (driving_hours >= 5),
    constraint course_details_lectures_hours_check
        check (lectures_hours >= 5),
    constraint course_details_price_check
        check (price >= (1000)::numeric)
);

create table instructors_permissions
(
    instructor_id bigint not null,
    permissions   varchar(255),
    constraint constraint_instructors_permissions_instructor_id_permission
        unique (instructor_id, permissions),
    constraint fk_instructors_permissions_instructor_id
        foreign key (instructor_id) references instructor_access
);

create table lecture_group
(
    id                bigint      not null,
    name              varchar(31) not null,
    course_category   varchar(1)  not null,
    version           bigint,
    creation_date     timestamp   not null,
    created_by        bigint      not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_lecture_group_id
        primary key (id),
    constraint constraint_lecture_group_name
        unique (name),
    constraint fk_lecture_group_created_by
        foreign key (created_by) references account,
    constraint fk_lecture_group_modified_by
        foreign key (modified_by) references account
);

create table lecture
(
    id                bigint    not null,
    instructor_id     bigint    not null,
    lecture_group_id  bigint    not null,
    date_from         timestamp not null,
    date_to           timestamp not null,
    version           bigint    not null,
    creation_date     timestamp not null,
    created_by        bigint    not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_lecture_id
        primary key (id),
    constraint fk_lecture_created_by
        foreign key (created_by) references account,
    constraint fk_lecture_modified_by
        foreign key (modified_by) references account,
    constraint fk_lecture_instructor_id
        foreign key (instructor_id) references instructor_access,
    constraint fk_lecture_lecture_group_id
        foreign key (lecture_group_id) references lecture_group,
    constraint booking_dates_check
        check (date_from < date_to)
);

create table course
(
    id                  bigint    not null,
    trainee_id          bigint    not null,
    course_details_id   bigint    not null,
    lecture_group_id    bigint,
    paid                boolean   not null,
    lectures_completion boolean   not null,
    course_completion   boolean   not null,
    version             bigint    not null,
    creation_date       timestamp not null,
    created_by          bigint    not null,
    modification_date   timestamp,
    modified_by         bigint,
    constraint pk_course_id
        primary key (id),
    constraint constraint_course_trainee_id_course_details_id
        unique (trainee_id, course_details_id),
    constraint fk_course_trainee_id
        foreign key (trainee_id) references trainee_access,
    constraint fk_course_course_details_id
        foreign key (course_details_id) references course_details,
    constraint fk_course_lecture_group_id
        foreign key (lecture_group_id) references lecture_group,
    constraint fk_course_created_by
        foreign key (created_by) references account,
    constraint fk_course_modified_by
        foreign key (modified_by) references account
);

create table driving_lesson
(
    id                bigint      not null,
    lesson_status     varchar(11) not null,
    instructor_id     bigint      not null,
    course_id         bigint      not null,
    car_id            bigint      not null,
    date_from         timestamp   not null,
    date_to           timestamp   not null,
    version           bigint      not null,
    creation_date     timestamp   not null,
    created_by        bigint      not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_driving_lesson_id
        primary key (id),
    constraint fk_driving_lesson_instructor_id
        foreign key (instructor_id) references instructor_access,
    constraint fk_driving_lesson_course_id
        foreign key (course_id) references course,
    constraint fk_driving_lesson_car_id
        foreign key (car_id) references car,
    constraint fk_driving_lesson_created_by
        foreign key (created_by) references account,
    constraint fk_driving_lesson_modified_by
        foreign key (modified_by) references account,
    constraint booking_dates_check
        check (date_from < date_to)
);

create table payment
(
    id                bigint        not null,
    payment_status    varchar(11)   not null,
    course_id         bigint        not null,
    value             numeric(4) not null,
    trainee_comment   varchar(255),
    admin_comment     varchar(255),
    version           bigint        not null,
    creation_date     timestamp     not null,
    created_by        bigint        not null,
    modification_date timestamp,
    modified_by       bigint,
    constraint pk_payment_id
        primary key (id),
    constraint fk_payment_course_id
        foreign key (course_id) references course,
    constraint fk_payment_created_by
        foreign key (created_by) references account,
    constraint fk_payment_modified_by
        foreign key (modified_by) references account,
    constraint payment_value_check
        check (value >= (0)::numeric)
);

create view auth_view (login, password, access) as
select a.login,
       a.password,
       ac.access_type as access
from account a
         join access ac on a.id = ac.account_id
where a.enabled = true
  and a.confirmed = true
  and ac.activated = true;