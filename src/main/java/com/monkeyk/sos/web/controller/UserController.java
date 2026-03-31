package com.monkeyk.sos.web.controller;

import com.monkeyk.sos.service.PasskeyService;
import com.monkeyk.sos.service.dto.UserFormDto;
import com.monkeyk.sos.service.dto.UserOverviewDto;
import com.monkeyk.sos.service.UserService;
import com.monkeyk.sos.web.controller.validator.UserFormDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

/**
 * @author Shengzhao Li
 */
@Controller
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserFormDtoValidator validator;

    @Autowired
    private PasskeyService passkeyService;

    /**
     * @return View page
     */
    @RequestMapping("overview")
    public String overview(UserOverviewDto overviewDto, Model model) {
        overviewDto = userService.loadUserOverviewDto(overviewDto);
        model.addAttribute("overviewDto", overviewDto);
        return "user_overview";
    }


    /**
     * 管理员查看指定用户绑定的 Passkey 凭据。
     *
     * @since 3.0.1
     */
    @RequestMapping("passkeys/{username}")
    public String userPasskeys(@PathVariable("username") String username, Model model) {
        model.addAttribute("targetUsername", username);
        if (!userService.isExistedUsername(username)) {
            model.addAttribute("errorMessage", "Username [" + username + "] not existed");
            model.addAttribute("passkeys", Collections.emptyList());
            return "passkeys/user_passkeys";
        }
        model.addAttribute("passkeys", passkeyService.loadUserPasskeys(username));
        return "passkeys/user_passkeys";
    }


    /**
     * 管理员删除指定用户的某个 Passkey。
     *
     * @since 3.0.1
     */
    @RequestMapping(value = "passkeys/{username}/delete/{credentialId}", method = RequestMethod.POST)
    public String deleteUserPasskey(@PathVariable("username") String username,
                                    @PathVariable("credentialId") String credentialId) {
        passkeyService.deleteUserPasskey(username, credentialId);
        return "redirect:../../" + username;
    }


    @RequestMapping(value = "form/plus", method = RequestMethod.GET)
    public String showForm(Model model) {
        model.addAttribute("formDto", new UserFormDto());
        return "user_form";
    }


    @RequestMapping(value = "form/plus", method = RequestMethod.POST)
    public String submitRegisterClient(@ModelAttribute("formDto") UserFormDto formDto, BindingResult result) {
        validator.validate(formDto, result);
        if (result.hasErrors()) {
            return "user_form";
        }
        userService.saveUser(formDto);
        return "redirect:../overview";
    }


}
