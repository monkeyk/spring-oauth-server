
-- 适用于: v3.0.0+ 版本 (OAuth2.1/OIDC)

=======================================
测试前准备
=======================================

需要在数据库 oauth2_boot 的 oauth2_registered_client 表中添加测试客户端（或者启动服务后在界面上'client_details'进行添加）:

client_id: 79763e77433640baba722e72c69e25db
client_secret: maAXVKPcEvVL37paPC0HTZU48DCG9Lat (BCrypt 加密存储)

具体配置请参考 others/oauth_test.txt

=======================================
测试用例
=======================================

## authorization_code  flow

1. start authorize (浏览器访问)
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=79763e77433640baba722e72c69e25db&scope=openid profile email&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p

2. 登录后重定向
   http://localhost:8083/oauth2/callback?code=xxx&state=93820ss0-32p

3. get access_token
- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
  --data-urlencode 'grant_type=authorization_code' \
  --data-urlencode 'redirect_uri=http://localhost:8083/oauth2/callback' \
  --data-urlencode 'code=xxx' \
  --data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat'

response
```
{
"access_token": "xxx",
"refresh_token": "xxx",
"scope": "openid profile email",
"id_token": "xxx",
"token_type": "Bearer",
"expires_in": 3599
}
```

4. refresh access_token
- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
  --data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat' \
  --data-urlencode 'grant_type=refresh_token' \
  --data-urlencode 'refresh_token=xxx'

5. get userinfo
- URL：http://localhost:8080/userinfo
- cURL
  curl --location 'http://localhost:8080/userinfo' \
  --header 'Authorization: Bearer <access_token>'

response
```
{
"sub": "admin",
"email": "admin@andaily.com",
"profile": "xxx"
}
```

## client_credentials  flow

- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
  --data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat' \
  --data-urlencode 'grant_type=client_credentials' \
  --data-urlencode 'scope=openid profile'

response
```
{
"access_token": "xxx",
"scope": "openid profile",
"token_type": "Bearer",
"expires_in": 3599
}
```

## authorization_code + PKCE flow
    Proof Key for Code Exchange (RFC7636)

1. start authorize (浏览器访问)
http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=79763e77433640baba722e72c69e25db&scope=openid profile&redirect_uri=http://localhost:8083/oauth2/callback&state=state9990988&code_challenge=HNxPXD6eoV_3eEWmd7Oktz_sYDRkgwUV39DAY97pmPc&code_challenge_method=S256

2. 登录后重定向
   http://localhost:8083/oauth2/callback?code=xxx&state=state9990988

3. get access_token
curl --location 'http://localhost:8080/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'redirect_uri=http://localhost:8083/oauth2/callback' \
--data-urlencode 'code=xxx' \
--data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat' \
--data-urlencode 'code_verifier=xxx'

## revoke token API
URL: http://localhost:8080/oauth2/revoke

curl --location 'http://localhost:8080/oauth2/revoke' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
--data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat' \
--data-urlencode 'token=xxx'

response: 200 [HTTP]

## introspect token API
URL: http://localhost:8080/oauth2/introspect

curl --location 'http://localhost:8080/oauth2/introspect' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=79763e77433640baba722e72c69e25db' \
--data-urlencode 'client_secret=maAXVKPcEvVL37paPC0HTZU48DCG9Lat' \
--data-urlencode 'token=xxx'

response
```
{
  "active": true,
  "scope": "openid profile",
  "client_id": "79763e77433640baba722e72c69e25db",
  "token_type": "Bearer",
  "exp": 1697724263,
  "iat": 1697720663
}
```

## OIDC Discovery
URL: http://localhost:8080/.well-known/openid-configuration

## JWKS
URL: http://localhost:8080/oauth2/jwks
