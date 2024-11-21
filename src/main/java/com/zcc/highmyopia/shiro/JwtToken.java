package com.zcc.highmyopia.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {


    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    //获取用户信息
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
