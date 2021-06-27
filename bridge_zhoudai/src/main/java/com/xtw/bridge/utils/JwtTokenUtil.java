package com.xtw.bridge.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: JWT令牌工具类
 */
@Data
@Component
public class JwtTokenUtil {
    // 通过配置文件给下面的变量赋值
    @Value("${jwt.secret}")
    private String secret;      // 加密/解密的密钥
    @Value("${jwt.expiration}")
    private Long expiration;    // 令牌的有效期(毫秒)
    @Value("${jwt.header}")
    private String header;      // HTTP请求头的key名称

    /**
     * 生成Token令牌
     * @param userDetails   用户
     * @return token令牌
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>(2);
        claims.put("sub",userDetails.getUsername());    // 用户名
        claims.put("created", new Date());      // 创建时间
        return generateToken(claims);       // 生成令牌
    }

    /**
     * 从令牌中获取用户名
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token){
        String username;
        try{
            Claims claims = getClaimsFromToken(token);  // 获取令牌中包含的用户信息
            username = claims.getSubject();     // 获取用户名
        } catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 判断令牌是否过期
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token){
        try{
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();   // 获取过期时间
            return expiration.before(new Date());       // 判断过期时间是否在当前日期之前(在之前就是过期了，返回true)
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 刷新令牌
     * @param token 令牌
     * @return
     */
    public String refreshToken(String token){
        String refreshToken;
        try{
            Claims claims = getClaimsFromToken(token);      // 从原令牌中获取其中包含的信息
            claims.put("created", new Date());      // 修改令牌的创建时间
            refreshToken = generateToken(claims);   // 生成新令牌
        } catch (Exception e){
            refreshToken = null;
        }
        return refreshToken;
    }

    /**
     * 验证令牌
     * @param token 令牌
     * @param userDetails 用户
     * @return 是否有效
     * Description: 将从数据库中获取的用户信息与令牌中的信息进行比对
     */
    public Boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        // 判断令牌中用户名和数据库中查到的用户名是否相同，token是否过期
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从clamis生成令牌，如果看不懂就看谁调用它
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims){
        // 到期时间
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);    // 当前时间 + 自定义时间
        return Jwts.builder()
                .setClaims(claims)      // 用户(包含用户名和创建时间)
                .setExpiration(expirationDate)  // 设置令牌到期时间
                .signWith(SignatureAlgorithm.HS512,secret)      // 签名的算法，secret是签名(解签)的密钥
                .compact();
    }

    /**
     * 从令牌中获取数据声明，如果看不懂就看谁调用它
     * @param token     令牌
     * @return      数据声明
     */
    private Claims getClaimsFromToken(String token){
        Claims claims;
        try{
            claims = Jwts.parser()  // 解析
                    .setSigningKey(secret)  // 解签的密钥
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e){
            claims = null;
        }
        return claims;
    }
}
