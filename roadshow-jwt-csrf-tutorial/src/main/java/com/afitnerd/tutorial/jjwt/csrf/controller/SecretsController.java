package com.afitnerd.tutorial.jjwt.csrf.controller;

import com.afitnerd.tutorial.jjwt.csrf.service.SecretService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecretsController extends BaseController {

    private SecretService secretService;

    public SecretsController(SecretService secretService) {
        this.secretService = secretService;
    }

    @GetMapping("/get-secrets")
    public Map<String, String> getSecrets() {
        return secretService.getSecrets();
    }

    @GetMapping("/refresh-secrets")
    public Map<String, String> refreshSecrets() {
        return secretService.refreshSecrets();
    }

    @PostMapping("/set-secrets")
    public Map<String, String> setSecrets(@RequestBody Map<String, String> secrets) {
        secretService.setSecrets(secrets);
        return secretService.getSecrets();
    }
}
