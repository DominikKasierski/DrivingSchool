---- Create user accounts ----
INSERT INTO account (id, enabled, confirmed, login, email_address, new_email_address, password, firstname, lastname, language,
                     phone_number, failed_login_attempts, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, true, true, 'kszczesniak', 'kszczeniak@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Krzysztof', 'Szczesniak', 'pl', '999888777', 0,
        now(), null, -1, null, 1),
       (-2, true, true, 'aadamski', 'adamadamski131@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Adam', 'Adamski', 'pl', '999888666', 0,
        now(), null, -1, null, 1),
       (-3, true, true, 'marbor', 'marbor123@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Marcin', 'Borowski', 'pl', '999888555', 0,
        now(), null, -1, null, 1),
       (-4, true, true, 'antek', 'antek123@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Antoni', 'Domagalski', 'pl', '999222555', 0,
        now(), null, -1, null, 1);

---- Create user accesses ----
INSERT INTO access (id, access_type, activated, account_id, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'ADMIN', true, -1, now(), null, -1, null, 1),
       (-2, 'INSTRUCTOR', true, -1, now(), null, -1, null, 1),
       (-3, 'INSTRUCTOR', true, -2, now(), null, -1, null, 1),
       (-4, 'TRAINEE', true, -3, now(), null, -1, null, 1),
       (-5, 'TRAINEE', true, -4, now(), null, -1, null, 1);

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

INSERT INTO instructors_permissions(instructor_id, permissions)
VALUES (-2, 'B'),
       (-2, 'C'),
       (-3, 'A'),
       (-3, 'B');

---- Create course details ----
INSERT INTO course_details (id, course_category, price, lectures_hours, driving_hours, creation_date, modification_date,
                            created_by, modified_by, version)
VALUES (-1, 'A', 2000, 30, 20, now(), null, -1, null, 1),
       (-2, 'B', 2200, 30, 30, now(), null, -1, null, 1),
       (-3, 'C', 3000, 20, 30, now(), null, -1, null, 1);

---- Create lecture groups ----
INSERT INTO lecture_group (id, name, course_category, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'PierwszaGrupaA', 'A', now(), null, -1, null, 1),
       (-2, 'PierwszaGrupaB', 'B', now(), null, -1, null, 1),
       (-3, 'PierwszaGrupaC', 'C', now(), null, -1, null, 1);

---- Create courses ----
INSERT INTO course (id, trainee_id, course_details_id, lecture_group_id, paid, lectures_completion, course_completion, creation_date,
                    modification_date, created_by, modified_by, version)
VALUES (-1, -4, -1, -1, false, false, false, now(), null, -3, null, 1),
       (-2, -5, -2, -2, false, false, false, now(), null, -4, null, 1);

---- Create payments ----
INSERT INTO payment (id, payment_status, course_id, value, trainee_comment, admin_comment, creation_date, modification_date,
                     created_by, modified_by, version)
VALUES (-1, 'REJECTED', -1, 750, 'Pierwsza wpłata', 'Błędna kwota', now(), null, -3, null, 1),
       (-2, 'IN_PROGRESS', -1, 700, 'Pierwsza wpłata - poprawiona', null, now(), null, -3, null, 1),
       (-3, 'CONFIRMED', -2, 500, 'Wpłata za kurs', null, now(), null, -4, null, 1),
       (-4, 'IN_PROGRESS', -2, 500, 'Wpłata z 8.05.2021r.', null, now(), null, -4, null, 1);

---- Create cars ----
INSERT INTO car (id, course_category, image, brand, model, registration_number, production_year, deleted, creation_date, modification_date,
                     created_by, modified_by, version)
VALUES (-1, 'A', '/static/motorcycle1.jpg', 'Yamaha', 'MT-07', 'ELEF646', 2015, false , now(), null, -1, null, 1),
       (-2, 'B', '/static/car1.jpg', 'KIA', 'Rio', 'ELE4646', 2016, false , now(), null, -1, null, 1),
       (-3, 'C', '/static/truck1.jpg', 'MAN', 'TGL 12.240', 'ELE646', 2014, false , now(), null, -1, null, 1);