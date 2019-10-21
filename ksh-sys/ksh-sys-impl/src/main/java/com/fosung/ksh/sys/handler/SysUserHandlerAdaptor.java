package com.fosung.ksh.sys.handler;

import com.fosung.framework.common.util.UtilAnnotation;
import com.fosung.framework.common.util.UtilBeanFactory;
import com.fosung.ksh.sys.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SysUserHandlerAdaptor {


    /**
     * 获取用户详细信息责任链
     *
     * @param hash
     * @return
     */
    public SysUser getUserByHash(String hash) {
        for (UserHandler userHandler : getUserHandlers()) {
            SysUser sysUser = userHandler.getUserByHash(hash);
            if (sysUser != null) {
                return sysUser;
            }
        }
        return null;
    }


    /**
     * 获取用户详细信息责任链
     *
     * @param hash
     * @return
     */
    public SysUser getSimpleUserByHash(String hash) {
        for (UserHandler userHandler : getUserHandlers()) {
            SysUser sysUser = userHandler.getSimpleUserByHash(hash);
            if (sysUser != null) {
                return sysUser;
            }
        }
        return null;
    }


    /**
     * 获取注入的用户登录验证链
     *
     * @return
     */
    private Collection<UserHandler> getUserHandlers() {
        Map<String, UserHandler> map = UtilBeanFactory.getApplicationContext().getBeansOfType(UserHandler.class);
        Collection<UserHandler> userHandlers = map.values();
        userHandlers = userHandlers.stream().sorted(new Comparator<UserHandler>() {
            @Override
            public int compare(UserHandler o1, UserHandler o2) {
                Integer value1 = getOrderValue(o1);
                Integer value2 = getOrderValue(o2);
                return value1 < value2 ? -1 : value1 > value2 ? 1 : 0;
            }
        }).collect(Collectors.toList());
        return userHandlers;
    }

    /**
     * 获取order顺序
     *
     * @param o1
     */
    private Integer getOrderValue(UserHandler o1) {
        Order order = UtilAnnotation.findAnnotation(o1.getClass(), Order.class);
        Integer orderValue = (Integer) UtilAnnotation.getValue(order);
        return orderValue;
    }


}
