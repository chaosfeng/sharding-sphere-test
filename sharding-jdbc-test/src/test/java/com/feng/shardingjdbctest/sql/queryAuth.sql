select distinct(a.*)
from auth a
where a.auth_id in (
	select ra.auth_id from role_auth ra where ra.role_id in (
		select ur.role_id from user_role ur where ur.user_id = (select u.user_id from t_user u where u.login_name = 'admin')
	)
);

select distinct(a.*)
from user_role ur
left join role_auth ra on ur.role_id = ra.role_id
left join auth a on ra.auth_id = a.auth_id
where ur.user_id = (select user_id from t_user where login_name='aaa');