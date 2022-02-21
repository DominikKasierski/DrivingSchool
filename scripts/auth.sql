create view auth_view (login, password, access) as
select a.login,
       a.password,
       ac.access_type as access
from account a join access ac on a.id = ac.account_id
where a.confirmed = true
  and a.enabled = true
  and ac.enabled = true;