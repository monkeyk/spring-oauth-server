package cc.wdcy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Shengzhao Li
 */
@Controller
@RequestMapping("/user/")
public class UserController {



    /**
     * Just forward to page
     *
     * @return View page
     */
    @RequestMapping("overview.htm")
    public String overview() {
        return "user_overview";
    }


}