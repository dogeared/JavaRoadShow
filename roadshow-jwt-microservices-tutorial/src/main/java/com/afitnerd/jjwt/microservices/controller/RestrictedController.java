package com.afitnerd.jjwt.microservices.controller;

import com.afitnerd.jjwt.microservices.model.AccountResponse;
import com.afitnerd.jjwt.microservices.service.AccountService;
import com.afitnerd.jjwt.microservices.service.SecretService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RestrictedController extends BaseController {

    private AccountService accountService;

    public RestrictedController(SecretService secretService, AccountService accountService) {
        super(secretService);
        this.accountService = accountService;
    }


    @RequestMapping("/restricted")
    public AccountResponse restricted(HttpServletRequest req) {
        return accountService.getAccount(req);
    }
}
