package com.afitnerd.tutorial.jjwt.csrf.controller;

import com.afitnerd.tutorial.jjwt.csrf.model.JwtResponse;
import com.afitnerd.tutorial.jjwt.csrf.service.SecretService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

@RestController
public class StaticJWTController extends BaseController {

    private SecretService secretService;

    public StaticJWTController(SecretService secretService) {
        this.secretService = secretService;
    }

    @GetMapping("/static-builder")
    public JwtResponse fixedBuilder() throws UnsupportedEncodingException {
        String jws = Jwts.builder()
            .setIssuer("Okta")
            .setSubject("msilverman")
            .claim("name", "Micah Silverman")
            .claim("scope", "admins")
            .setIssuedAt(Date.from(Instant.ofEpochSecond(1561393128L)))   // Mon Jun 24 2019
            .setExpiration(Date.from(Instant.ofEpochSecond(4717066728L))) // Sat Jun 24 2119
            .signWith(
                SignatureAlgorithm.HS256,
                secretService.getHS256SecretBytes()
            )
            .compact();

        return new JwtResponse(jws);
    }

    @GetMapping("/parser")
    public JwtResponse parser(@RequestParam String jwt) throws UnsupportedEncodingException {

        Jws<Claims> jws = Jwts.parser()
            .setSigningKeyResolver(secretService.getSigningKeyResolver())
            .parseClaimsJws(jwt);

        return new JwtResponse(jws);
    }

    @GetMapping("/parser-enforce")
    public JwtResponse parserEnforce(@RequestParam String jwt) throws UnsupportedEncodingException {
        Jws<Claims> jws = Jwts.parser()
            .requireIssuer("Okta")
            .require("hasMotorcycle", true)
            .setSigningKeyResolver(secretService.getSigningKeyResolver())
            .parseClaimsJws(jwt);

        return new JwtResponse(jws);
    }
}
