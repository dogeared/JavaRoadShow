package com.afitnerd.tutorial.jjwt.csrf.controller;

import com.afitnerd.tutorial.jjwt.csrf.model.JwtResponse;
import com.afitnerd.tutorial.jjwt.csrf.service.SecretService;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@RestController
public class DynamicJWTController extends BaseController {

    private SecretService secretService;

    public DynamicJWTController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping("/dynamic-builder-general")
    public JwtResponse dynamicBuilderGeneric(@RequestBody Map<String, Object> claims) throws UnsupportedEncodingException {
        String jws =  Jwts.builder()
            .setClaims(claims)
            .signWith(
                SignatureAlgorithm.HS256,
                secretService.getHS256SecretBytes()
            )
            .compact();
        return new JwtResponse(jws);
    }

    @PostMapping("/dynamic-builder-compress")
    public JwtResponse dynamicBuildercompress(@RequestBody Map<String, Object> claims) throws UnsupportedEncodingException {
        String jws =  Jwts.builder()
            .setClaims(claims)
            .compressWith(CompressionCodecs.DEFLATE)
            .signWith(
                SignatureAlgorithm.HS256,
                secretService.getHS256SecretBytes()
            )
            .compact();
        return new JwtResponse(jws);
    }

    @PostMapping("/dynamic-builder-specific")
    public JwtResponse dynamicBuilderSpecific(@RequestBody Map<String, Object> claims) throws UnsupportedEncodingException {
        JwtBuilder builder = Jwts.builder();

        claims.forEach((key, value) -> {
            switch (key) {
                case "iss":
                    ensureType(key, value, String.class);
                    builder.setIssuer((String) value);
                    break;
                case "sub":
                    ensureType(key, value, String.class);
                    builder.setSubject((String) value);
                    break;
                case "aud":
                    ensureType(key, value, String.class);
                    builder.setAudience((String) value);
                    break;
                case "exp":
                    ensureType(key, value, Long.class);
                    builder.setExpiration(Date.from(Instant.ofEpochSecond(Long.parseLong(value.toString()))));
                    break;
                case "nbf":
                    ensureType(key, value, Long.class);
                    builder.setNotBefore(Date.from(Instant.ofEpochSecond(Long.parseLong(value.toString()))));
                    break;
                case "iat":
                    ensureType(key, value, Long.class);
                    builder.setIssuedAt(Date.from(Instant.ofEpochSecond(Long.parseLong(value.toString()))));
                    break;
                case "jti":
                    ensureType(key, value, String.class);
                    builder.setId((String) value);
                    break;
                default:
                    builder.claim(key, value);
            }
        });

        builder.signWith(SignatureAlgorithm.HS256, secretService.getHS256SecretBytes());

        return new JwtResponse(builder.compact());
    }

    private void ensureType(String registeredClaim, Object value, Class expectedType) {
        boolean isCorrectType =
            expectedType.isInstance(value) ||
            expectedType == Long.class && value instanceof Integer;

        if (!isCorrectType) {
            String msg = "Expected type: " + expectedType.getCanonicalName() + " for registered claim: '" +
                registeredClaim + "', but got value: " + value + " of type: " + value.getClass().getCanonicalName();
            throw new JwtException(msg);
        }
    }
}
