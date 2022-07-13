---- Create user accounts ----
INSERT INTO account (id, enabled, confirmed, login, email_address, new_email_address, password, firstname, lastname, language,
                     phone_number, failed_login_attempts, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, true, true, 'kszczesniak', 'kszczeniak@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Krzysztof', 'Szcześniak', 'pl', '999888777', 0,
        now(), null, -1, null, 1),
       (-2, true, true, 'aadamski', 'adamadamski131@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Adam', 'Adamski', 'pl', '999888666', 0,
        now(), null, -1, null, 1),
       (-3, true, true, 'marbor', 'marbor123@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Marcin', 'Borowski', 'pl', '999888555', 0,
        now(), null, -1, null, 1),
       (-4, true, true, 'antek', 'antek123@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Antoni', 'Domagalski', 'pl', '999222555', 0,
        now(), null, -1, null, 1),
       (-5, true, true, 'tomek', 'tomek@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Tomasz', 'Tomkowski', 'pl', '999223555', 0,
        now(), null, -1, null, 1),
       (-6, true, true, 'KonradL', 'konrad@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Konrad', 'Lis', 'pl', '999000555', 0,
        now(), null, -1, null, 1),
       (-7, true, true, 'Bartek20', 'bartek@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Bartek', 'Barański', 'pl', '123223555', 0,
        now(), null, -1, null, 1),
       (-8, true, true, 'wkowalski', 'wojtek@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Wojtek', 'Kowalski', 'pl', '532223555', 0,
        now(), null, -1, null, 1);

---- Create user accesses ----
INSERT INTO access (id, access_type, activated, account_id, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'ADMIN', true, -1, now(), null, -1, null, 1),
       (-2, 'INSTRUCTOR', true, -1, now(), null, -1, null, 1),
       (-3, 'INSTRUCTOR', true, -2, now(), null, -1, null, 1),
       (-4, 'TRAINEE', true, -3, now(), null, -1, null, 1),
       (-5, 'TRAINEE', true, -4, now(), null, -1, null, 1),
       (-6, 'TRAINEE', true, -5, now(), null, -1, null, 1),
       (-7, 'TRAINEE', true, -6, now(), null, -1, null, 1),
       (-8, 'TRAINEE', true, -7, now(), null, -1, null, 1),
       (-9, 'TRAINEE', true, -8, now(), null, -1, null, 1);

---- Create access extension tables ----
INSERT INTO admin_access (id)
VALUES (-1);
INSERT INTO instructor_access (id)
VALUES (-2);
INSERT INTO instructor_access (id)
VALUES (-3);
INSERT INTO trainee_access (id)
VALUES (-4);
INSERT INTO trainee_access (id)
VALUES (-5);
INSERT INTO trainee_access (id)
VALUES (-6);
INSERT INTO trainee_access (id)
VALUES (-7);
INSERT INTO trainee_access (id)
VALUES (-8);
INSERT INTO trainee_access (id)
VALUES (-9);

INSERT INTO instructors_permissions(instructor_id, permissions)
VALUES (-2, 'A'),
       (-2, 'B'),
       (-2, 'C'),
       (-3, 'A'),
       (-3, 'B'),
       (-3, 'C');

---- Create course details ----
INSERT INTO course_details (id, course_category, price, lectures_hours, driving_hours, creation_date, modification_date,
                            created_by, modified_by, version)
VALUES (-1, 'A', 2000, 30, 20, now(), null, -1, null, 1),
       (-2, 'B', 2200, 30, 30, now(), null, -1, null, 1),
       (-3, 'C', 3000, 20, 30, now(), null, -1, null, 1);

---- Create lecture groups ----
INSERT INTO lecture_group (id, name, course_category, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'Ghost Riderzy', 'A', now(), null, -1, null, 1),
       (-2, 'Driverzy', 'B', now(), null, -1, null, 1),
       (-3, 'Rajdowcy', 'C', now(), null, -1, null, 1);

---- Create courses ----
INSERT INTO course (id, trainee_id, course_details_id, lecture_group_id, advance, paid, lectures_completion, driving_completion,
                    course_completion, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, -4, -1, -1, false, false, false, false, false, now(), null, -3, null, 1),
       (-2, -5, -2, -2, false, false, false, false, false, now(), null, -4, null, 1),
       (-3, -6, -2, null, true, false, false, false, false, now(), null, -5, null, 1),
       (-4, -7, -1, null, true, true, false, false, false, now(), null, -6, null, 1),
       (-5, -8, -1, null, true, false, false, false, false, now(), null, -7, null, 1),
       (-6, -9, -3, -3, true, true, true, false, false, now(), null, -8, null, 1);

---- Create payments ----
INSERT INTO payment (id, payment_status, course_id, value, trainee_comment, admin_comment, creation_date, modification_date,
                     created_by, modified_by, version)
VALUES (-1, 'REJECTED', -1, 750, 'Pierwsza wpłata', 'Błędna kwota', now() - INTERVAL '1 day', null, -3, null, 1),
       (-2, 'IN_PROGRESS', -1, 700, 'Pierwsza wpłata - poprawiona', null, now() - INTERVAL '12 hour', null, -3, null, 1),
       (-3, 'CONFIRMED', -2, 500, 'Wpłata za kurs', null, now() - INTERVAL '4 hour', null, -4, null, 1),
       (-4, 'IN_PROGRESS', -2, 500, 'Wpłata z wczoraj', null, now() - INTERVAL '1 hour', null, -4, null, 1),
       (-5, 'CONFIRMED', -3, 1500, null, null, now() - INTERVAL '2 day', null, -1, null, 1),
       (-6, 'CONFIRMED', -4, 2000, null, 'Opłacone pierwszego dnia', now() - INTERVAL '1 day', null, -1, null, 1),
       (-7, 'Cancelled', -5, 1000, 'Wpłata z poniedziałku', null, now() - INTERVAL '2 day', null, -7, null, 1),
       (-8, 'CONFIRMED', -5, 1050, 'Wpłata z poniedziałku', null, now() - INTERVAL '2 day', null, -7, null, 1),
       (-9, 'CONFIRMED', -6, 3000, null, null, now() - INTERVAL '5 day', null, -1, null, 1);

---- Create cars ----
INSERT INTO car (id, course_category, image, brand, model, registration_number, production_year, deleted, creation_date,
                 modification_date, created_by, modified_by, version)
VALUES (-1, 'A', '/static/motorcycle1.jpg', 'Yamaha', 'MT-07', 'ELEF646', 2015, false, now(), null, -1, null, 1),
       (-2, 'B', '/static/car1.jpg', 'KIA', 'Rio', 'ELE4646', 2016, false, now(), null, -1, null, 1),
       (-3, 'C', '/static/truck1.jpg', 'MAN', 'TGL 12.240', 'ELE646', 2014, false, now(), null, -1, null, 1);

---- Create lectures ----
INSERT INTO lecture (id, instructor_id, lecture_group_id, date_from, date_to, creation_date, modification_date, created_by, modified_by,
                      version)
VALUES (-1, -2, -3, date '2022-07-01' + time '16:00', date '2022-07-01' + time '20:00', date '2022-06-30', null, -1, null, 1),
       (-2, -2, -3, date '2022-07-04' + time '16:00', date '2022-07-04' + time '20:00', date '2022-06-30', null, -1, null, 1),
       (-3, -3, -3, date '2022-07-06' + time '16:00', date '2022-07-06' + time '20:00', date '2022-07-04', null, -1, null, 1),
       (-4, -3, -3, date '2022-07-07' + time '16:00', date '2022-07-07' + time '20:00', date '2022-07-04', null, -1, null, 1),
       (-5, -2, -3, date '2022-07-08' + time '16:00', date '2022-07-08' + time '20:00', date '2022-07-06', null, -1, null, 1);

---- Create driving lessons ----
INSERT INTO driving_lesson (id, lesson_status, instructor_id, course_id, car_id, date_from, date_to, creation_date, modification_date,
                             created_by, modified_by, version)
VALUES (-1, 'FINISHED', -3, -6, -3, date '2022-07-11' + time '16:00', date '2022-07-11' + time '18:00',
        date '2022-07-08' + time '21:00', null, -8, null, 1),
       (-2, 'IN_PROGRESS', -2, -6, -3, date '2022-07-12' + time '17:00', date '2022-07-12' + time '19:00',
        date '2022-07-08' + time '21:00', null, -8, null, 1),
       (-3, 'PENDING', -3, -6, -3, date '2022-07-20' + time '15:00', date '2022-07-20' + time '17:00',
        date '2022-07-08' + time '21:00', null, -8, null, 1);
