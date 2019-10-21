package com.fosung.ksh.oauth2.server.dt.secure.converter;

import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.common.util.UtilAuthentication;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.mvc.config.secure.configurer.authentication.context.converter.AppAuthenticationConverterOAuth2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基于灯塔sso登录的认证对象转换器
 * @Author : liupeng
 * @Date : 2019-01-06
 * @Modified By
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AppAuthenticationConverterOAuth2DT extends AppAuthenticationConverterOAuth2 {

    public static final String USER_ATTRIBUTE_TELEPHONE  = "telephone" ;

    public static final String USER_ATTRIBUTE_LOGO  = "logo" ;

    public static final String USER_ATTRIBUTE_HASH  = "hash" ;



    public AppAuthenticationConverterOAuth2DT(String registrationId){
        super( registrationId ) ;
    }

    /**
     * 获取用户属性值
     * @param oauth2User
     * @return
     */
    public String getUserAttribute( OAuth2User oauth2User , String attributeKey ){
        Object userHash = oauth2User.getAttributes().get( attributeKey ) ;

        return userHash!=null ? userHash.toString() : null ;
    }

    @Override
    public Object loadUserId(OAuth2User oauth2User) {
        log.info("loadUserId");
        // 使用hash作为灯塔用户的id
        return getUserAttribute( oauth2User , USER_ATTRIBUTE_HASH );
    }

    /**
     * 执行其它的属性转换
     * @param appUserDetailsDefault
     * @param oauth2User
     */
    @Override
    public void convertMore(AppUserDetailsDefault appUserDetailsDefault , OAuth2User oauth2User ){
        log.info("convertMore");
        // 设置用户手机号信息
        appUserDetailsDefault.setTelephone( getUserAttribute( oauth2User , USER_ATTRIBUTE_TELEPHONE ) );
        appUserDetailsDefault.setAvatar( getUserAttribute( oauth2User , USER_ATTRIBUTE_LOGO ) );

        this.loadUserOrg( appUserDetailsDefault , oauth2User ) ;
    }

    /**
     * 装载用户组织机构信息
     * @param appUserDetailsDefault
     * @param oauth2User
     */
    public void loadUserOrg(AppUserDetailsDefault appUserDetailsDefault , OAuth2User oauth2User ){
//        if( appDTUserService==null ){
//            return ;
//        }

        log.info("loadUserOrg");

    }

    @Override
    public Collection<GrantedAuthority> loadUserAuthorities(OAuth2User oauth2User) {
        Object roles = oauth2User.getAttributes().get("roles") ;
        if( roles==null || !(roles instanceof List) ){
            return Lists.newArrayList() ;
        }

        // 抽取返回的用户角色列表
        Set<String> userRoles = Sets.newHashSet() ;
        ((List<Object>)roles).stream().filter( role-> role instanceof Map).forEach(role->{
            Object roleCode = ((Map)role).get("roleName") ;
            if( roleCode!=null ){
                userRoles.add( roleCode.toString() ) ;
            }
        });

        log.info("登录用户: {} , 包含角色: {}" , oauth2User.getName() , UtilString.joinByComma( userRoles ));

        return UtilAuthentication.convertRoleToGrantedAuthority( userRoles );
    }
}
