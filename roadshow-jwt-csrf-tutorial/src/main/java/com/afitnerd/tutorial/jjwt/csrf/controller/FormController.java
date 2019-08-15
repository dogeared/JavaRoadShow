package com.afitnerd.tutorial.jjwt.csrf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormController {

    @GetMapping("/jwt-csrf-form")
    public String csrfFormGet() {
        return "jwt-csrf-form";
    }

    @PostMapping("/jwt-csrf-form")
    public String csrfFormPost(@RequestParam(name = "_csrf") String csrf, Model model) {
        model.addAttribute("csrf", csrf);
        return "jwt-csrf-form-result";
    }

    @RequestMapping("/expired-jwt")
    public String expiredJwt() {
        return "expired-jwt";
    }
}
