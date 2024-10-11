package com.example.springboot.springboot.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JWTTool {
    @Value("${security.jwt.secret}")
    private String key;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${securrty.jwt.ttlMillis}")
    private long ttlMillis;

    public String create(String id, String subject) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKey = DatatypeConverter.parseBase64Binary(key);
        Key singInKey = new SecretKeySpec(apiKey, algorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(algorithm, singInKey);

        if (ttlMillis >= 0) {
            builder.setExpiration(new Date(nowMillis + ttlMillis));
        }
        return builder.compact();
    }

    public String getValue(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public String getKey(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt)
                .getBody()
                .getId();
    }

}
