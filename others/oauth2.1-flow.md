
    v3.0.0+ used


## authorization_code  flow

1. start authorize
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile email&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile phone&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p

2. response code
   http://localhost:8083/oauth2/callback?code=-VEnyAcEflDxjMh4Hr-6YejZq4Mel5gihFy_FMyotDxLhILeMBQheJkL4mdJ0sKD_C8xpa_sMNGf_I2tYJIVki8a4ktT2QsHojhbV3HpbGLVhJ0qDc8kfXjWt7u_24QO&state=93820ss0-32p

3. get access_token

- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/json' \
  --form 'client_id="client11"' \
  --form 'grant_type="authorization_code"' \
  --form 'redirect_uri="http://localhost:8083/oauth2/callback"' \
  --form 'code="-VEnyAcEflDxjMh4Hr-6YejZq4Mel5gihFy_FMyotDxLhILeMBQheJkL4mdJ0sKD_C8xpa_sMNGf_I2tYJIVki8a4ktT2QsHojhbV3HpbGLVhJ0qDc8kfXjWt7u_24QO"' \
  --form 'client_secret="secret22"'

response
{
"access_token": "eyJraWQiOiIyZGZjNTczMi1kODkyLTQ4NjMtYjZkMS04YTgzOGE3NzZmZTUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImNsaWVudDExIiwibmJmIjoxNjkwNzk0MDk2LCJzY29wZSI6WyJyZWFkIl0sImlzcyI6Imh0dHBzOi8vbXlvaWRjLmNvbSIsImV4cCI6MTY5MDc5NDM5NiwiaWF0IjoxNjkwNzk0MDk2fQ.pK2hUiQwi_9FWw6aXYSjtJJbPmzZQI_A9zbrtw4p-talPSf9IV9U0aSBboaO0SXmvwcLbWPb8TF1tY2DX8osMezscDiv8U7K3bxUQR7nVrrjrS60ExfvrT_r2IAs9no4fr11e_NnQIzCHDy87qqFPbu0QUSPbJpD0L_t019g7E8LEb_2EqO_4-SvP8tNdmLUrPnDOndbDtkAQB7GXpEB4uyhS0KW_VaoWKZgXu4IcUa927C151LI0wvQXiVATilMm_soUIeZEvFm9ilxXC2OpsNKIPfLIQYNbzUm8juRPOI38BUTyMGBe9qtH1IC5CDOZuBuTzs7Owhdy7Bu2zbf-w",
"refresh_token": "xYCsaPu7YV_hB6TfLbWsFBws1YvP7D_qAJFlSCvT5u-RbP6uMwEudHZaVnoyw3wuaXO-8F3t_GYMNZyfFVTAGBHyYDs9VS6_vqbLqqL0mGMI20GOGY066bdRTOtFlwsl",
"scope": "read",
"token_type": "Bearer",
"expires_in": 299
}


4. refresh access_token

- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/json' \
  --form 'client_id="client11"' \
  --form 'grant_type="refresh_token"' \
  --form 'refresh_token="xYCsaPu7YV_hB6TfLbWsFBws1YvP7D_qAJFlSCvT5u-RbP6uMwEudHZaVnoyw3wuaXO-8F3t_GYMNZyfFVTAGBHyYDs9VS6_vqbLqqL0mGMI20GOGY066bdRTOtFlwsl"' \
  --form 'client_secret="secret22"'

response
{
"access_token": "eyJraWQiOiIyZGZjNTczMi1kODkyLTQ4NjMtYjZkMS04YTgzOGE3NzZmZTUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImNsaWVudDExIiwibmJmIjoxNjkwNzk2MjA5LCJzY29wZSI6WyJyZWFkIl0sImlzcyI6Imh0dHBzOi8vbXlvaWRjLmNvbSIsImV4cCI6MTY5MDc5NjUwOSwiaWF0IjoxNjkwNzk2MjA5fQ.RjMZHpzz2YgK9ov_v4C94hWPS9qA9EiAVXvV9jxA9l4xLbzVVrmfC4w4QE7Z-8femjhtzzeZj5oCe1hO9v3WfSHXhO_5DAH4S9GY5acDo4XAUBbIKg1r4nvzE3QXacLbRDdtKSn62TM44NTPI_XjBU58e2EuZVRpPaOR5tEQpHjygDS3TW7aC2gouugm0f9YQCCPiHWrUQuA5cWiMJ0E8G_Q9GifVLkSy0aI7wzyvxhnKvd0Xoa5y6rHv3f2Whul5YwHo3aqHDfCO1AaUbCRknFJgG-LYyOj8iUvci2vCyPeWZ1uGm33a5s1PYcxqXXzmRvRvk1ZjMStETg00p-3kA",
"refresh_token": "xYCsaPu7YV_hB6TfLbWsFBws1YvP7D_qAJFlSCvT5u-RbP6uMwEudHZaVnoyw3wuaXO-8F3t_GYMNZyfFVTAGBHyYDs9VS6_vqbLqqL0mGMI20GOGY066bdRTOtFlwsl",
"scope": "read",
"token_type": "Bearer",
"expires_in": 299
}


5. get userinfo
- URL：http://localhost:8080/userinfo
- cURL
  curl --location 'http://localhost:8080/userinfo' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Bearer eyJraWQiOiJteW9pZGMta2V5aWQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImNsaWVudDExIiwibmJmIjoxNjkyMDg0OTQ2LCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODc4MSIsImV4cCI6MTY5MjA5MjE0NiwiaWF0IjoxNjkyMDg0OTQ2LCJqdGkiOiJkMDI0NTNhNS0xNmRmLTRiZGYtOTBhMS1lOGYyYjMxOWY5YzMifQ.hvVjgkGHsmDfFZia-B4H1D3vo03Yuj0Kd2KvF-EGuS9BzZTzvee8XetiRO-C6mqRw1s-Wa6wZB4QwB9-WyLc7tpu0TgfKDDn71nJQNZ2QgzcNIUlclxG5K21mVMmrA-c4Le5HGPLWsGItDkpqA1OtgL4U622kGHrf0RJCmpC_WxPnECYsI84dgILE6n9s27UZQhYtYLiq5aoovvHImrztTClRmNTwc4iB9RX_gpb9YFs0diMWvIBgDokEAJE_K9BY0HZqpqj7T1ilecfbcv_T2Ebd8JnnZyCTUcpIyZ4DlWqzvnEp70cz945NuaYQG-_VPSjhGiymsNxWkP0HMGRuQ' \

response
{
"sub": "admin",
"updated_at": "123456990",
"nickname": "xxx"
}


## client_credentials  flow

- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/json' \
  --form 'client_id="client11"' \
  --form 'grant_type="client_credentials"' \
  --form 'client_secret="secret22"'

response
{
"access_token": "eyJraWQiOiJmYjliY2Q4Ni0yMDExLTRjYjYtOGQ4Yi03MmJmZjMwMTVjZGQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjbGllbnQxMSIsImF1ZCI6ImNsaWVudDExIiwibmJmIjoxNjkwNzk2ODc4LCJpc3MiOiJodHRwczovL215b2lkYy5jb20iLCJleHAiOjE2OTA4MDQwNzgsImlhdCI6MTY5MDc5Njg3OH0.YL9R0J_EuCSiarZnWWZxvPTdElxBs6r6B3FJ-nkI6paMwaCaWtSyIUqK_5GiF-uiRSK5me_8g4E8OOKitncYhXwVGpplvKXoIQousubkTqtjEhpegCR1i9J9xLL0vJuVsTz8sd8vdd0rDY9QSyeH5Xq2mayRD8il0LArO3QbT9PDM7uKW8PQ2YlxqkqxIRd-tZyyV3eRBzrNvvBb3mPqjyEsSI6c89L5Cs-lhdW5FJR5f7eKS1jUcl6jNWv3xOOWD2J-SjMnCDepGHQHDdxO_sfKZPKBSNThBWqX_4XQBMAxOlRxZLjHjymFYY-xusLh0AbQHrG7xRLyBEW3zNDSNw",
"token_type": "Bearer",
"expires_in": 7199
}

## authorization_code + PKCE  flow
    Proof Key for Code Exchange (RFC7636)

1. start authorize

http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile&redirect_uri=http://localhost:8083/oauth2/callback&state=state9990988&code_challenge=HNxPXD6eoV_3eEWmd7Oktz_sYDRkgwUV39DAY97pmPc&code_challenge_method=S256


2. response code
   http://localhost:8083/oauth2/callback?code=Laulaadi78kB0DkQKvCPv96KMk56s8NQjwA3lJ_IagKn1u3x-5jrTBATu_5rZDLsXq89Lp4nNjAqYMnQjohz8WFV5Ql9R0Bj46w7yYkT8hfTEEGkHYxJC8K3Qf6_riF0&state=state9990988

3. get access_token

curl --location 'http://localhost:8080/oauth2/token' \
--header 'Content-Type: application/json' \
--form 'client_id="client11"' \
--form 'grant_type="authorization_code"' \
--form 'redirect_uri="http://localhost:8083/oauth2/callback"' \
--form 'code="Laulaadi78kB0DkQKvCPv96KMk56s8NQjwA3lJ_IagKn1u3x-5jrTBATu_5rZDLsXq89Lp4nNjAqYMnQjohz8WFV5Ql9R0Bj46w7yYkT8hfTEEGkHYxJC8K3Qf6_riF0"' \
--form 'client_secret="secret22"' \
--form 'code_verifier="OXhHcFQ5TWIzSTdBUGJ0RlBZZm5xUEN2QnIzSkpyTXFCOVlSMHFBd2ZCSmhjZ1FK"'

response
{
"access_token": "eyJraWQiOiJteW9pZGMta2V5aWQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImNsaWVudDExIiwibmJmIjoxNjkyNzYyNjA5LCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODc4MSIsImV4cCI6MTY5Mjc2OTgwOSwiaWF0IjoxNjkyNzYyNjA5LCJqdGkiOiJkNmRlZGVmNi1lYmFhLTRjOTEtYjhjZC1kM2QxZGQ2OTIzNzEifQ.Fuuu9jI1uXEevvJswgqvsyR0PZkvn8ijYX3PjDhJj4_t_L0U0DbWTJNr8-dQWVA2AuIjlLs_5SsI8mq_sZOfZc8TBZRhJYbSiluLoNKxaHTHfMimY0Zb712x2mZ9NS_DzEPJeNLTTxvm0X7mmLgoXdc2hYSEbXVYicIGaidIBy6rFaSMyA5bdmSoI3gfwW2PQ58NBHDQDkEZmWmLZ6ZkLKGANzSpWUmraA7lhV_UphmHqk55kcgqEWQKNqD3x6OZ20jpUgtrkr6TjbtFmjMOYV7r0_jMGihmPSjXoXYspDcrS9T9fE9oW7_rSe1YUnQaR9s5ghkqFCki7WS7Tnzj-w",
"refresh_token": "VWbIs3Ls2pAZknHSXGV5oH_VHNQwoiWmSDQi0UbQesApSWR1xpYB2Ggyct4iCzITKE5STJEbRPKZUTJNvuFfWFv3rgJYD4ggZ0nHnkQ3GQ_a471DxWU--smzwRpb4vxx",
"scope": "openid profile",
"id_token": "eyJraWQiOiJlY2Mta2lkLTEiLCJhbGciOiJFUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6ImNsaWVudDExIiwidXBkYXRlZF9hdCI6IjEyMzQ1Njk5MCIsImF6cCI6ImNsaWVudDExIiwiYXV0aF90aW1lIjoxNjkyNzYyNTQ2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3ODEiLCJuaWNrbmFtZSI6Inh4eCIsImV4cCI6MTY5Mjc2NDQwOSwiaWF0IjoxNjkyNzYyNjA5LCJqdGkiOiJkZDM2ZGEyNy1lYTI4LTRlM2YtOTk5My01NDgyNzI0ZmE5NWUiLCJzaWQiOiJZZWNCLUo2Xy14Nlo0YnZiOW43RGIweDJIYy12bk5VWVpoSGNjNUVfM293In0.cT7k6P8IQNpGHiX4B1GB4wDxOUltvWM0PlyLWDQLk5tD3gnU-JvaGre2QeJBUeYLyZG17iZQWvfAxMAFpSolFQ",
"token_type": "Bearer",
"expires_in": 7199
}




## DEVICE_CODE  flow
Class: OAuth2DeviceAuthorizationEndpointFilter

1. device call device_authorization

curl --location 'http://localhost:8080/oauth2/device_authorization' \
--header 'Content-Type: application/json' \
--form 'client_id="client11"' \
--form 'client_secret="secret22"' \
--form 'scope="openid"'

response
{
"user_code": "VZRP-KNJR",
"device_code": "GQ-K6n5kwLfu3XpDja-b3SlPbTfqYirFtO4JpWmniLUWCSRO2Suu09ipNUo1srUBwebN2PP9KqfIoVpW6DE94xNDkBS-ExAINFHnZzm4XvV5kHOXQwT2UTx5OQp6_7qz",
"verification_uri_complete": "http://localhost:8080/oauth2/device_verification?user_code=VZRP-KNJR",
"verification_uri": "http://localhost:8080/oauth2/device_verification",
"expires_in": 300
}

2. Logged user visit  verification_uri_complete  using a browser (or another authorized device use QR and so on)
   http://localhost:8080/oauth2/device_verification
   then type user_code and submit the form

Class: OAuth2DeviceVerificationEndpointFilter

3. device get token

request
curl --location 'http://localhost:8080/oauth2/token' \
--header 'Content-Type: application/json' \
--form 'client_id="client11"' \
--form 'grant_type="urn:ietf:params:oauth:grant-type:device_code"' \
--form 'client_secret="secret22"' \
--form 'device_code="voqSMpNJAvVlMBQ1_R65a_MMWD344YKQqrlo86JG-VeFRz6iCMdhn5VBLwbNoHaidP9db33BJDaLWHHtpEP98NpwEf9wre_X-o8kq1_Dg8aj0r9lRP5aH-ZNI8wpon6b"'

response [200]
{
"access_token": "eyJraWQiOiJteW9pZGMta2V5aWQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoiY2xpZW50MTEiLCJuYmYiOjE2OTI3ODU5MDEsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxIiwiZXhwIjoxNjkyNzkzMTAxLCJpYXQiOjE2OTI3ODU5MDEsImp0aSI6ImRlOGExYjkwLWI1MzAtNDJmMi1iOWVlLTMwZDQ1ZTkxNDkwNiJ9.dkMOrM74R9eg7ozTtT8a0Tfe7TOjQ0gl0LZ-eG4eOyCUGuSGILzOrF_X7UQ4eaOAu43mRiJOJ81uqOcMgiMj6ijh4ihXumg0EnnWj79ShMX-4bCgy3d8_Ioq5E7CqykZkg0JIQNRm2Mv5Ei6PiPriBDCVjghCbz-UcPoT_66a-dIE3rO95LEQDCoFkWulq93HC9XfPbD2RwAWpRp1C3nD9GToNxkll9zeW8KZm46WSm-ihM6AG2VRj1dhIAOvodwPqGnc9gcY8KKT6tEEeuOSIMlWInQ4YG-pGM0n4h2OxhACZ8AfqQstdm2FP7o83ejVvBIreaaiA15BgqiqFx6IA",
"refresh_token": "UCFNxUj4ytr241KzwJJgnMno1RfmoLs0GKVxNWPjW5VZ7d4U4YsDM7CwNgxRu7sKfDMFH2lcW_xcXA6CD7dztzUi41SaQZLkwfWU64F40al6U7x0sP3tfhgT005fS43g",
"scope": "openid profile",
"token_type": "Bearer",
"expires_in": 7199
}

or [400]
{
"error": "authorization_pending",
"error_uri": "https://datatracker.ietf.org/doc/html/rfc8628#section-3.5"
}



## JWT_BEARER flow



## revoke token API
Class: OAuth2TokenRevocationEndpointFilter
URL: http://localhost:8080/oauth2/revoke

curl --location 'http://localhost:8080/oauth2/revoke' \
--header 'Content-Type: application/json' \
--form 'client_id="client11"' \
--form 'client_secret="secret22"' \
--form 'token="{token}"'

response
200 [HTTP]

## introspect token API
Class: OAuth2TokenIntrospectionEndpointFilter
URL: http://localhost:8080/oauth2/introspect

curl --location 'http://localhost:8080/oauth2/introspect' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=2EF5AAF1492717B75C29750E806E789D' \
--form 'client_id="client11"' \
--form 'client_secret="secret22"' \
--form 'token="{token}"'

response
{
"active": true,
"sub": "client11",
"aud": [
"client11"
],
"nbf": 1690979995,
"scope": "openid profile",
"iss": "http://localhost:8080",
"exp": 1690987195,
"iat": 1690979995,
"client_id": "client11",
"token_type": "Bearer"
}


## logout token API
Class: OidcLogoutEndpointFilter
URL: http://localhost:8080/connect/logout?id_token_hint=${id_token}&post_logout_redirect_uri=${post_logout_redirect_uri}&state=${state}




## .well-known URL

URL: http://localhost:8080/.well-known/openid-configuration
Class: OidcProviderConfigurationEndpointFilter
Response: {"issuer":"http://localhost:8080","authorization_endpoint":"http://localhost:8080/oauth2/authorize","device_authorization_endpoint":"http://localhost:8080/oauth2/device_authorization","token_endpoint":"http://localhost:8080/oauth2/token","token_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"jwks_uri":"http://localhost:8080/oauth2/jwks","userinfo_endpoint":"http://localhost:8080/userinfo","end_session_endpoint":"http://localhost:8080/connect/logout","response_types_supported":["code"],"grant_types_supported":["authorization_code","client_credentials","refresh_token","urn:ietf:params:oauth:grant-type:device_code"],"revocation_endpoint":"http://localhost:8080/oauth2/revoke","revocation_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"introspection_endpoint":"http://localhost:8080/oauth2/introspect","introspection_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"subject_types_supported":["public"],"id_token_signing_alg_values_supported":["RS256"],"scopes_supported":["openid"]}

URL: http://localhost:8080/.well-known/oauth-authorization-server
Class: OAuth2AuthorizationServerMetadataEndpointFilter
Response: {"issuer":"http://localhost:8080","authorization_endpoint":"http://localhost:8080/oauth2/authorize","device_authorization_endpoint":"http://localhost:8080/oauth2/device_authorization","token_endpoint":"http://localhost:8080/oauth2/token","token_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"jwks_uri":"http://localhost:8080/oauth2/jwks","response_types_supported":["code"],"grant_types_supported":["authorization_code","client_credentials","refresh_token","urn:ietf:params:oauth:grant-type:device_code"],"revocation_endpoint":"http://localhost:8080/oauth2/revoke","revocation_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"introspection_endpoint":"http://localhost:8080/oauth2/introspect","introspection_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"code_challenge_methods_supported":["S256"]}


## reference doc

https://springdoc.cn/spring-authorization-server/index.html

https://developer.aliyun.com/article/1050110

在线PKCE生成工具
https://tonyxu-io.github.io/pkce-generator/
