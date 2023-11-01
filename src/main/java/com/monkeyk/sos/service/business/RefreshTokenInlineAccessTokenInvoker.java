package com.monkeyk.sos.service.business;



/**
 * 2019/7/5
 * <p>
 * <p>
 * grant_type = refresh_token
 *
 * @author Shengzhao Li
 * @since 2.0.1
 */
public class RefreshTokenInlineAccessTokenInvoker extends InlineAccessTokenInvoker {


    public RefreshTokenInlineAccessTokenInvoker() {
    }

//    @Override
//    protected TokenGranter getTokenGranter(OAuth2RequestFactory oAuth2RequestFactory) {
//        return new RefreshTokenGranter(this.tokenServices, this.clientDetailsService, oAuth2RequestFactory);
//    }


}
