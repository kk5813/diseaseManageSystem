package com.zcc.highmyopia.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.zcc.highmyopia.entity.User;
import com.zcc.highmyopia.service.UserService;
import com.zcc.highmyopia.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
* 自定义Realm，扩展功能，需要继承AuthorizingRealm类并重写doGetAuthenticationInfo方法
*
* */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //自定义授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //自定义的Shiro的登录认证方法，shiro的方法底层会调用该类的认证方法进行认证
    //需要配置自定义的realm生效，在ini文件中配置或在Springboot中配置
    //该方法只是获取进行对比的信息，认证逻辑还是按照shiro底层认证逻辑完成
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取身份信息并转为jwt令牌，
        JwtToken jwtToken = (JwtToken) authenticationToken;
        //获取凭证信息
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        //获取数据库中存储的用户信息
        User user = userService.getById(Long.valueOf(userId));
        if(user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        if(user.getUserStatus() == -1) {
            throw new DisabledAccountException("账户已被锁定");
        }
        //创建封装校验逻辑对象，封装数据返回
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }

}
