package com.lcy.reggie.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.security.*;
import java.util.Date;
import java.util.Map;
@Component
public class JwtRsaUtils {
    private static final Long expire = 43200000L;
    private static KeyPair keyPair;

    static {
        try {
            // 生成RSA密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public static PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */


    public String generateJwt(Map<String, Object> claims) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(JwtRsaUtils.getPrivateKey());



        return Jwts.builder()
                .addClaims(claims)
                .signWith((Key) signature, SignatureAlgorithm.RS256)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public Claims parseJWT(String jwt){

        Claims claims = Jwts.parser()
                .setSigningKey(JwtRsaUtils.getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}

