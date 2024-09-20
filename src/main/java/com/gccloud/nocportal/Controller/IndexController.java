package com.gccloud.nocportal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(path = "/")
    public String goToLoginPage(Model theModel) {

        return "redirect:/noc/portal";
    }

}
