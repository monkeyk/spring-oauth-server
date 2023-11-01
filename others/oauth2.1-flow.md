
    v3.0.0+ used


## authorization_code  flow
Core-Class: OAuth2AuthorizationEndpointFilter

1. start authorize

   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile email&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p
   http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=client11&scope=openid profile phone&redirect_uri=http://localhost:8083/oauth2/callback&state=93820ss0-32p

2. response code

   http://localhost:8083/oauth2/callback?code=-VEnyAcEflDxjMh4Hr-6YejZq4Mel5gihFy_FMyotDxLhILeMBQheJkL4mdJ0sKD_C8xpa_sMNGf_I2tYJIVki8a4ktT2QsHojhbV3HpbGLVhJ0qDc8kfXjWt7u_24QO&state=93820ss0-32p

3. get access_token
- Core-Class: OAuth2TokenEndpointFilter

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
"access_token": "7154afT_cxvLDq1naSg6Aq9ueSFSW8xRr5txryW5MlddRe7nV0RogTYwPsJc_rrRqwaIvLleerLhkjtIN2E2U-4J_BzvYNCsv8BVLqeerCObwgwpP3t__NMMUakzRL2i",
"refresh_token": "TZ9tzVwE_VLoJxALUSw4A4A0Nj7SLSWXCc69U9rvNmSnqR8Hbz-1m4uHebJWsAK0sa7SDIR4SNXOB3iaM0p1bH_8EBrljoBApQgdYi1uYzcVwYq55OVV2RUHN2BJwfSr",
"scope": "openid profile",
"id_token": "eyJraWQiOiJzb3MtZWNjLWtpZDEiLCJhbGciOiJFUzI1NiJ9.eyJzdWIiOiJ1bml0eSIsImF1ZCI6IjZ1ck5MZ1I2b3NrMkU1NmVrcCIsInVwZGF0ZWRfYXQiOiIiLCJhenAiOiI2dXJOTGdSNm9zazJFNTZla3AiLCJhdXRoX3RpbWUiOjE2OTc3MDczNTQsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6ODA4MCIsIm5pY2tuYW1lIjoiIiwiZXhwIjoxNjk3NzA5MjA4LCJpYXQiOjE2OTc3MDc0MDgsImp0aSI6IjEyNTc0MjU2NTk4MDI2ODY2NzI3NDAwMTMxNjk5NDk0Iiwic2lkIjoidXdwN255RnJwdlNtWmlQS2hCdWVSVFZfcVRKYkN6ZjAyTmYwQTZGN1lrSSJ9.3w-7EY9SwKA-UkXlhDfD2BbSwP6nCSLZxNgKwhkkMY8YPbMkygbj374SmEmsit7NlpRXHCtW6ULZ9_IVZ9MTBg",
"token_type": "Bearer",
"expires_in": 3599
}


4. refresh access_token

- Core-Class: OAuth2TokenEndpointFilter

- URL: http://localhost:8080/oauth2/token      [POST]
- cURL
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/json' \
  --form 'client_id="6urNLgR6osk2E56ekp"' \
  --form 'client_secret="6urNLgR6osk2E56ekp"' \
  --form 'grant_type="refresh_token"' \
  --form 'refresh_token="TZ9tzVwE_VLoJxALUSw4A4A0Nj7SLSWXCc69U9rvNmSnqR8Hbz-1m4uHebJWsAK0sa7SDIR4SNXOB3iaM0p1bH_8EBrljoBApQgdYi1uYzcVwYq55OVV2RUHN2BJwfSr"'

response

{
"access_token": "YnVdTXl0MhslsrOjiz1ffSixvPnWCN-XS-UBlkS89daZbd_TvXtSSo_ODuFVWPWw1KsO5WQykVPjwSe_Kreo8ngIP9DglaXJMbYJJu4Wa6_geOINj5ksmnbfb6pHrQHr",
"refresh_token": "TZ9tzVwE_VLoJxALUSw4A4A0Nj7SLSWXCc69U9rvNmSnqR8Hbz-1m4uHebJWsAK0sa7SDIR4SNXOB3iaM0p1bH_8EBrljoBApQgdYi1uYzcVwYq55OVV2RUHN2BJwfSr",
"scope": "openid profile",
"id_token": "eyJraWQiOiJzb3MtZWNjLWtpZDEiLCJhbGciOiJFUzI1NiJ9.eyJzdWIiOiJ1bml0eSIsImF1ZCI6IjZ1ck5MZ1I2b3NrMkU1NmVrcCIsInVwZGF0ZWRfYXQiOjAsImF6cCI6IjZ1ck5MZ1I2b3NrMkU1NmVrcCIsImF1dGhfdGltZSI6MTY5NzcwNzM1NCwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgwIiwibmlja25hbWUiOiIiLCJleHAiOjE2OTc3MjQyNjMsImlhdCI6MTY5NzcyMjQ2MywianRpIjoiMDc4OTc4MTUxNzEwNTgwNDE2ODY0NzgxMDQ1OTM5MDYiLCJzaWQiOiJ1d3A3bnlGcnB2U21aaVBLaEJ1ZVJUVl9xVEpiQ3pmMDJOZjBBNkY3WWtJIn0.j0KVv7bAi85zbX-0wvWe83n_CQdmJLGrHJNFwF5jA1-wa8QzaSwJbznpjbHLGTv-UbI2YeHLn8N5iGXDarbC9Q",
"token_type": "Bearer",
"expires_in": 3599
}


5. get userinfo
- Core-Class: OidcUserInfoEndpointFilter
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
  --form 'client_id="6urNLgR6osk2E56ekp"' \
  --form 'client_secret="6urNLgR6osk2E56ekp"' \
  --form 'grant_type="client_credentials"' \
  --form 'scope="openid profile"'

response

{
"access_token": "p2i1WHiiFBCgTJFTs63OvO9-bclB9DbsgsebDo_ntMw_BAleu2RzIQzzFfaaJAR5oiL3xwN3xMyNTRZSrXM_1ANycleysPU5l3xuZ0aQX4V-Va178qg6e-PvLqLBsD_i",
"scope": "openid profile",
"token_type": "Bearer",
"expires_in": 3599
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
Core-Class: OAuth2DeviceAuthorizationEndpointFilter

1. device call device_authorization

curl --location 'http://localhost:8080/oauth2/device_authorization' \
--header 'Content-Type: application/json' \
--form 'client_id="6urNLgR6osk2E56ekp"' \
--form 'client_secret="6urNLgR6osk2E56ekp"' \
--form 'scope="openid profile"'

response

{
"user_code": "PCKJ-FWZS",
"device_code": "ZPMq2sfyHPj_pJ78T6J4yGcsAAi_XbuBjtQz2NLxYWKDHbcqUhg2nFHe3Ynp3V1SyCOwYEoaz9lPvqt-oj0sXKxJDnC5usJmANVqMQ-8Qjpp1ROi9gljdQY2NO3YYvIo",
"verification_uri_complete": "http://127.0.0.1:8080/oauth2/device_verification?user_code=PCKJ-FWZS",
"verification_uri": "http://127.0.0.1:8080/oauth2/device_verification",
"expires_in": 300
}

2. Logged user visit  verification_uri_complete  using a browser (or another authorized device use QR and so on)
   http://localhost:8080/oauth2/device_verification
   then type user_code and submit the form

Core-Class: OAuth2DeviceVerificationEndpointFilter

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
"access_token": "QqPGuiF9c2HKYQEdxrs9E0WsRijEl_z9sINI6CFD5yMulXaZutLTktVtLP3zcr22XuYJOzWZMzOgvjWl2tqAoMo3S2MHBgxjPmx5gfr6DjeQPsW3fFPVc6pOa5Ll6u4S",
"refresh_token": "7vtQtkU95tjt7nkaX8DZnDVntrgPYIoXB6_4WsV9FzMi-ppoPB_H5qmufi4EHqAuJPwdlxXYdDbVYoGudXd0iCPfmqT5B8CcW7zRsgaKQOHQlPw9Ju3wMGNSRk14YRWI",
"scope": "profile",
"token_type": "Bearer",
"expires_in": 3599
}

or [400]

{
"error": "authorization_pending",
"error_uri": "https://datatracker.ietf.org/doc/html/rfc8628#section-3.5"
}



## JWT_BEARER flow
- Core-Class: JwtClientAssertionAuthenticationProvider
- URL: http://localhost:8080/oauth2/token

- grant_type=authorization_code
curl --location 'http://localhost:8080/oauth2/token' \
--header 'Content-Type: application/json' \
--form 'client_id="vLIXDF9GXg6Psfh1uzwVFUj0fucX2Zn9"' \
--form 'client_assertion_type="urn:ietf:params:oauth:client-assertion-type:jwt-bearer"' \
--form 'scope="openid"' \
--form 'grant_type="authorization_code"' \
--form 'client_assertion="eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ2TElYREY5R1hnNlBzZmgxdXp3VkZVajBmdWNYMlpuOSIsInN1YiI6InZMSVhERjlHWGc2UHNmaDF1endWRlVqMGZ1Y1gyWm45IiwiYXVkIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgwIiwiZXhwIjoxNjk4MTE5NjMxfQ.-40zh9Sao9JzP4_eYVnIpreuk76Nql4ue3hNuyhu59c"' \
--form 'code="CyN4YB2Y9p8y1lqfUQc0_jxbuL0spqP8pC8vriwzwKP4AQhtYriMVF-obChcf83rwLILZP8z-uSVKcS-eGvZPE-vTM-LbiMXic0tXW1fzWfYd0r7ijGapX1Nnho3-XWn"' \
--form 'redirect_uri="https://andaily.com/oauth2/callback"'

- grant_type=client_credentials
  curl --location 'http://localhost:8080/oauth2/token' \
  --header 'Content-Type: application/json' \
  --form 'client_id="dofOx6hjxlWw9qe2bnFvqbiPhuWwGWdn"' \
  --form 'client_assertion_type="urn:ietf:params:oauth:client-assertion-type:jwt-bearer"' \
  --form 'scope="openid"' \
  --form 'grant_type="client_credentials"' \
  --form 'client_assertion="eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJkb2ZPeDZoanhsV3c5cWUyYm5GdnFiaVBodVd3R1dkbiIsInN1YiI6ImRvZk94NmhqeGxXdzlxZTJibkZ2cWJpUGh1V3dHV2RuIiwiYXVkIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgwIiwiZXhwIjoxNjk4MzI4NDI0fQ.A-CMlBoOqtlWVQiu8RjK9xWKG4lqBMT7IMCVIDJc3hsSZk7KvApL2lPx3k2b9bDM8Ysr7VXnFPfQbN8RN4sTsf2x-cpzDQ-vFBGMFqgaXZckuba21moT42GWyTULQ2_HRYy8bLCfOiX7BG4HyJYHf2JDrZgQ3pPu3VhH5D9bJ5_y6WcZxDlVMBUMXGRuhwl0tCTc8L0Ss3azPD82wMblDavCUTxNzOvb0qc3orVEjgUW77cxzGi929TtWtCvBH8dyNh_CAsvYJKAJDskTnLKv6GihL33pNHBhfjwSUP2s-_LPD6Z7gjf9GJHSSz7TeztX3NU9-FaoJZjYGR2lq2F2A"' \
  --form 'client_secret="dofOx6hjxlWw9qe2bnFvqbiPhuWwGWdn"'



## revoke token API
Core-Class: OAuth2TokenRevocationEndpointFilter

URL: http://localhost:8080/oauth2/revoke

curl --location 'http://localhost:8080/oauth2/revoke' \
--header 'Content-Type: application/json' \
--form 'client_id="6urNLgR6osk2E56ekp"' \
--form 'client_secret="6urNLgR6osk2E56ekp"' \
--form 'token="TZ9tzVwE_VLoJxALUSw4A4A0Nj7SLSWXCc69U9rvNmSnqR8Hbz-1m4uHebJWsAK0sa7SDIR4SNXOB3iaM0p1bH_8EBrljoBApQgdYi1uYzcVwYq55OVV2RUHN2BJwfSr"'

response

200 [HTTP]

## introspect token API
Core-Class: OAuth2TokenIntrospectionEndpointFilter

URL: http://localhost:8080/oauth2/introspect

curl --location 'http://localhost:8080/oauth2/introspect' \
--header 'Content-Type: application/json' \
--form 'client_id="6urNLgR6osk2E56ekp"' \
--form 'client_secret="6urNLgR6osk2E56ekp"' \
--form 'token="GaHu88XEEAz41xMHfDk05bg9uSJ5Go1RF6jOe5eX7OhHD_52NK_fuwvVWq_dTRIhK8WR9SnCAtBBc0fVsOyGgz8-MhmVTG-dcDi6QtGQQtYxwmGrD-fOhpmePdUv6pwV"'

response

{
"active": true,
"sub": "admin",
"aud": [
"6urNLgR6osk2E56ekp"
],
"nbf": 1697721873,
"scope": "openid profile",
"iss": "http://127.0.0.1:8080",
"exp": 1697725474,
"iat": 1697721874,
"jti": "a1aa8f82-c885-45b3-a469-c2f595e8f12d",
"client_id": "6urNLgR6osk2E56ekp",
"token_type": "Bearer"
}


## logout token API
Core-Class: OidcLogoutEndpointFilter

URL: http://localhost:8080/connect/logout?id_token_hint=${id_token}&client_id={client_id}&post_logout_redirect_uri=${post_logout_redirect_uri}&state=${state}




## .well-known URL
### OIDC 1.0
- URL: http://localhost:8080/.well-known/openid-configuration
- Core-Class: OidcProviderConfigurationEndpointFilter
- Response: {"issuer":"http://localhost:8080","authorization_endpoint":"http://localhost:8080/oauth2/authorize","device_authorization_endpoint":"http://localhost:8080/oauth2/device_authorization","token_endpoint":"http://localhost:8080/oauth2/token","token_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"jwks_uri":"http://localhost:8080/oauth2/jwks","userinfo_endpoint":"http://localhost:8080/userinfo","end_session_endpoint":"http://localhost:8080/connect/logout","response_types_supported":["code"],"grant_types_supported":["authorization_code","client_credentials","refresh_token","urn:ietf:params:oauth:grant-type:device_code"],"revocation_endpoint":"http://localhost:8080/oauth2/revoke","revocation_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"introspection_endpoint":"http://localhost:8080/oauth2/introspect","introspection_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"subject_types_supported":["public"],"id_token_signing_alg_values_supported":["RS256"],"scopes_supported":["openid"]}

### OAuth 2.1
- URL: http://localhost:8080/.well-known/oauth-authorization-server
- Core-Class: OAuth2AuthorizationServerMetadataEndpointFilter
- Response: {"issuer":"http://localhost:8080","authorization_endpoint":"http://localhost:8080/oauth2/authorize","device_authorization_endpoint":"http://localhost:8080/oauth2/device_authorization","token_endpoint":"http://localhost:8080/oauth2/token","token_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"jwks_uri":"http://localhost:8080/oauth2/jwks","response_types_supported":["code"],"grant_types_supported":["authorization_code","client_credentials","refresh_token","urn:ietf:params:oauth:grant-type:device_code"],"revocation_endpoint":"http://localhost:8080/oauth2/revoke","revocation_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"introspection_endpoint":"http://localhost:8080/oauth2/introspect","introspection_endpoint_auth_methods_supported":["client_secret_basic","client_secret_post","client_secret_jwt","private_key_jwt"],"code_challenge_methods_supported":["S256"]}


## jwks URL
- URL: http://localhost:8080/oauth2/jwks
- Core-Class: NimbusJwkSetEndpointFilter
- Response: {"keys":[{"kty":"EC","crv":"P-256","kid":"sos-ecc-kid1","key_ops":["sign","deriveKey","decrypt","encrypt","verify"],"x":"UyCuPXhC0_KLRqfWPNDU4ZljSx7lQ_vP7VbYDiOZmsk","y":"2HuQhn3bfkmYiB6BLQKlN8tkI8awkeOiKaNk1cu06ow","alg":"ES256"},{"kty":"RSA","e":"AQAB","kid":"sos-rsa-kid2","key_ops":["deriveKey","verify","encrypt","decrypt","sign"],"alg":"RS256","n":"st2IswiZyQXHy86KBYQdEYv3sAfWpyx-e4o0Dcqvpck0E1FpZfVcFzbLy9B7YHvXv1SseVcg93iiNYgGlPDeZxPllz4-oIisDvSmEJdAidhqQxxpMeSjeQzvVu4CKjGFG9jA68pTm-KDia3Y516b4tPyKhHGIUZq2yJrNIs2QjTikYbn5AxAQ244cDPTsuEV5yqdOdyWvdlrn4WSFLiPt31MboT6et7Hmm90fwbMDSaWWb2XNo2gOnzWFwlNO2s8zK_Z1IWhmreb_XH5mW9xirrT03nbnLTLcmLtZYHFKjP55zRFDgKsXeo9BQNG3dkCsWz0N8pURaN6cuXYoYGU7Q"}]}


---
## reference doc

https://springdoc.cn/spring-authorization-server/index.html

https://developer.aliyun.com/article/1050110

[jwt-bearer] https://developer.atlassian.com/cloud/jira/software/user-impersonation-for-connect-apps/

在线PKCE生成工具
1. PKCEUtils.java
2. https://tonyxu-io.github.io/pkce-generator/

