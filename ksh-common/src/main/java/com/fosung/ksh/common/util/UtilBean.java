package com.fosung.ksh.common.util;

import com.fosung.framework.common.util.UtilBeanProperty;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Column;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author wangyh
 */
public class UtilBean extends UtilBeanProperty {

    /**
     * 忽略公共属性
     */
    public static final List<String> ignoreNames = new ArrayList<String>() {
        {
            add("id");
            add("createUserId");
            add("createDatetime");
            add("lastUpdateUserId");
            add("lastUpdateDatetime");
            add("del");
        }
    };

    /**
     * 获取对象中全部属性名称
     *
     * @return
     */
    public static Set<String> getPropertyNames(Class clazz) {
        Set<String> set = Sets.newHashSet();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && !ignoreNames.contains(field.getName())) {

                set.add(field.getName());
            }
        }
        return set;
    }

    /**
     * 获取对象中全部属性名称
     *
     * @return
     */
    public static Set<String> getPropertyNames(Class clazz,Collection<String> excludes) {
        Set<String> set = Sets.newHashSet();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && !ignoreNames.contains(field.getName()) && !excludes.contains(field.getName())) {
                set.add(field.getName());
            }
        }
        return set;
    }


    /**
     * Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
     *
     * @param map
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void transMap2Bean2(Map<String, Object> map, Object obj) throws InvocationTargetException, IllegalAccessException {
        if (map == null || obj == null) {
            return;
        }

        BeanUtils.populate(obj, map);

    }

    /**
     * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
     *
     * @param map
     * @param obj
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            if (map.containsKey(key)) {
                Object value = map.get(key);
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }

        }


    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);

                map.put(key, value);
            }

        }

        return map;

    }
}
