package com.afitnerd.tutorial.jjwt.csrf.config;

import com.afitnerd.tutorial.jjwt.csrf.service.SecretService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
public class CSRFConfig {

    private SecretService secretService;

    public CSRFConfig(SecretService secretService) {
        this.secretService = secretService;
    }

    @Bean
    @ConditionalOnMissingBean
    public CsrfTokenRepository jwtCsrfTokenRepository() {
        return new JWTCSRFTokenRepository(secretService.getHS256SecretBytes());
    }
}
