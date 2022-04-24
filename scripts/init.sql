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
        now(), null, -1, null, 1);

---- Create user accesses ----
INSERT INTO access (id, access_type, activated, account_id, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'ADMIN', true, -1, now(), null, -1, null, 1),
       (-2, 'INSTRUCTOR', true, -1, now(), null, -1, null, 1),
       (-3, 'INSTRUCTOR', true, -2, now(), null, -1, null, 1),
       (-4, 'TRAINEE', true, -3, now(), null, -1, null, 1);

---- Create access extension tables ----
INSERT INTO admin_access (id)
VALUES (-1);
INSERT INTO instructor_access (id)
VALUES (-2);
INSERT INTO instructor_access (id)
VALUES (-3);
INSERT INTO trainee_access (id)
VALUES (-4);