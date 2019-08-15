package com.afitnerd.jjwt.microservices.controller;

import com.afitnerd.jjwt.microservices.model.AccountResponse;
import com.afitnerd.jjwt.microservices.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestrictedController extends BaseController {

    @Autowired
    AccountService accountService;


    @RequestMapping("/restricted")
    public AccountResponse restricted(HttpServletRequest req) {
        return accountService.getAccount(req);
    }
}
