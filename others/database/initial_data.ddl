-- Initial database  data

truncate user_;
truncate user_privilege;
-- admin, password is Admin@2013  ( All privileges)
insert into user_(id, guid, create_time, email, password, phone, username, default_user)
values (21, '29f6004fb1b0466f9572b02bf2ac1be8', now(), 'admin@andaily.com',
        '$2a$10$bIIt6KqIMweTZZC.IIHBLuN3dEIJL0LQFRPrtWTujn9O3Sl5Us5vW', '028-1234567', 'admin', 1);

insert into user_privilege(user_id, privilege)
values (21, 'ADMIN');
insert into user_privilege(user_id, privilege)
values (21, 'UNITY');
insert into user_privilege(user_id, privilege)
values (21, 'MOBILE');

-- unity, password is Unity#2013  ( ROLE_UNITY)
insert into user_(id, guid, create_time, email, password, phone, username, default_user)
values (22, '55b713df1c6f423e842ad68668523c49', now(), 'unity@andaily.com',
        '$2a$10$M/bdEKNH12ksSmMgt0p3YeSjW4C5auAjE8by9BY6oEkHTjGKNDqTO', '', 'unity', 0);

insert into user_privilege(user_id, privilege)
values (22, 'UNITY');

-- mobile, password is Mobile*2013  ( ROLE_MOBILE)
insert into user_(id, guid, create_time, email, password, phone, username, default_user)
values (23, '612025cb3f964a64a48bbdf77e53c2c1', now(), 'mobile@andaily.com',
        '$2a$10$MJKW44F.e.UH.54OY36b6eCPpp8KRszL3vAgqLyL1WWnpbGp7A8zW', '', 'mobile', 0);

insert into user_privilege(user_id, privilege)
values (23, 'MOBILE');


-- initial oauth client details test data
-- 'unity-client'   support browser device visit,  secret:  unity
-- 'mobile-client'  only support mobile-device visit,  secret:  mobile
truncate oauth2_registered_client;
insert into oauth2_registered_client
(id, create_time, client_id, client_secret, client_name, client_authentication_methods,
 authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
values ('851eee5eaba94b0cacca53a3ef543423', now(), 'unity-client',
        '$2a$10$QQTKDdNfj9sPjak6c8oWaumvTsa10MxOBOV6BW3DvLWU6VrjDfDam',
        'Unity-Client',
        'client_secret_post,client_secret_jwt,client_secret_basic',
        'refresh_token,urn:ietf:params:oauth:grant-type:device_code,authorization_code',
        'http://localhost:8080/unity/dashboard', null, 'openid,profile,email',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","ES256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",172800.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",120.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}'),
       ('aedd67f6dae441b99e3a0fb27889ce12', now(), 'mobile-client',
        '$2a$10$uLvpxfvm3CuUyjIvYq7a9OUmd9b3tHFKrUaMyU/jC01thrTdkBDVm',
        'Mobile-Client',
        'client_secret_post,client_secret_basic',
        'refresh_token,password',
        null, null, 'openid,profile',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","ES256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",172800.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",120.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');


