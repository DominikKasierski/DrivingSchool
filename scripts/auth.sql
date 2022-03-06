create view auth_view (login, password, access) as
select a.login,
       a.password,
       ac.access_type as access
from account a join access ac on a.id = ac.account_id
where a.blocked = false
  and a.confirmed = true
  and ac.activated = true;