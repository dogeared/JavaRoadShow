package com.afitnerd.tutorial.jjwt.csrf.controller;

import com.afitnerd.tutorial.jjwt.csrf.service.SecretService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SecretsController extends BaseController {

    private SecretService secretService;

    public SecretsController(SecretService secretService) {
        this.secretService = secretService;
    }

    @RequestMapping(value = "/get-secrets", method = GET)
    public Map<String, String> getSecrets() {
        return secretService.getSecrets();
    }

    @RequestMapping(value = "/refresh-secrets", method = GET)
    public Map<String, String> refreshSecrets() {
        return secretService.refreshSecrets();
    }

    @RequestMapping(value = "/set-secrets", method = POST)
    public Map<String, String> setSecrets(@RequestBody Map<String, String> secrets) {
        secretService.setSecrets(secrets);
        return secretService.getSecrets();
    }
}
