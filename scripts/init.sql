---- Create user accounts ----
INSERT INTO account (id, enabled, confirmed, login, email_address, new_email_address, password, firstname, lastname, language,
                     phone_number, failed_login_attempts, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, true, true, 'kszczesniak', 'kszczeniak@gmail.com', null,
        'bdd2297f93550f01452cbd838c276f0dd22f498b4661394f1528ab88d6e63e6f', 'Krzysztof', 'Szczesniak', 'pl', '999888777', 0,
        now(), null, -1, null, 1);

---- Create user accesses ----
INSERT INTO access (id, access_type, activated, account_id, creation_date, modification_date, created_by, modified_by, version)
VALUES (-1, 'ADMIN', true, -1, now(), null, -1, null, 1);

---- Create access extension tables ----
INSERT INTO admin_access (id)
VALUES (-1);