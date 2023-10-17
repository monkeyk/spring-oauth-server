package com.monkeyk.sos.web.controller;


import com.monkeyk.sos.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2018/4/19
 * <p>
 * starup
 *
 * @author Shengzhao Li
 */
@Controller
public class SOSController {


    private static final Logger LOG = LoggerFactory.getLogger(SOSController.class);


    /**
     * 首页
     */
    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }


    //Go login
    @GetMapping(value = {"/login"})
    public String login(Model model) {
        LOG.info("Go to login, IP: {}", WebUtils.getIp());
        return "login";
    }


//    /**
//     * 403 无权限访问
//     *
//     * @return view
//     * @since 3.0.0
//     */
//    @GetMapping("/access_denied")
//    public String accessDenied() {
//        return "access_denied";
//    }


}
