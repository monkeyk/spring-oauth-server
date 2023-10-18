package com.monkeyk.sos.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.monkeyk.sos.domain.shared.SOSConstants.DEVICE_VERIFICATION_ENDPOINT_URI;


/**
 * 2023/10/17 18:49
 * <p>
 * Device code flow use
 *
 * @author Shengzhao Li
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2DeviceVerificationEndpointFilter
 * @since 3.0.0
 */
@Controller
public class OAuth2DeviceVerificationController {


    /**
     * Device verification page
     *
     * @return view
     */
    @RequestMapping(value = DEVICE_VERIFICATION_ENDPOINT_URI, method = {RequestMethod.GET, RequestMethod.POST})
    public String deviceVerification() {
        return "device_verification";
    }


}
