package com.monkeyk.sos.service.business;



/**
 * 2019/7/5
 * <p>
 * <p>
 * grant_type = client_credentials
 *
 * @author Shengzhao Li
 * @since 2.0.1
 */
public class ClientCredentialsInlineAccessTokenInvoker extends InlineAccessTokenInvoker {


    public ClientCredentialsInlineAccessTokenInvoker() {
    }

//    @Override
//    protected TokenGranter getTokenGranter(OAuth2RequestFactory oAuth2RequestFactory) {
//        return new ClientCredentialsTokenGranter(this.tokenServices, this.clientDetailsService, oAuth2RequestFactory);
//    }


}
