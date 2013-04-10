package com.trekker.security.shiro;

import com.trekker.model.User;
import com.trekker.service.UserService;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class JpaRealm extends AuthorizingRealm {
    UserService service;
    
    public JpaRealm() throws NamingException {
        setName("JpaRealm");
        service = (UserService)new InitialContext().lookup("java:global/trekker/UserService");
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)at;
        User user = service.find(token.getUsername());
        if (user != null) {
            return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), getName());
        } else {
            throw new AuthenticationException();
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        // Just return a new SimpleAuthorizationInfo, we won't implement roles
        return new SimpleAuthorizationInfo();
    }
}
