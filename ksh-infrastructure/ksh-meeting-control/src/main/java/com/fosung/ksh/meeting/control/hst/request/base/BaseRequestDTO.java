package com.fosung.ksh.meeting.control.hst.request.base;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.util.UtilBeanProperty;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.Order;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;

/**
 * 基类，提供排序规则
 *
 * @author wangyh
 */
public class BaseRequestDTO implements Serializable {

    private static final long serialVersionUID = -8302523785809233340L;

    /**
     * 根据注解的顺序，获取bean中的属性所对应的值，并返回对应的数组
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public Object[] getValues() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Object> list = Lists.newArrayList();
        List<JSONObject> orderList = sort();

        //对排序后的结果转为数组
        for (JSONObject jsonObject : orderList) {
            String name = jsonObject.getString("name");
            Object obj = UtilBeanProperty.getProperty(this, name);
            list.add(obj);
        }
        return list.toArray();
    }

    /**
     * 根据order注解进行排序，返回排序结果
     *
     * @return
     */
    private List<JSONObject> sort() {
        List<JSONObject> orderList = Lists.newArrayList();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Order order = field.getAnnotation(Order.class);
            if (order != null) {
                int orderNumber = order.value();
                String name = field.getName();
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("orderNumber", orderNumber);
                orderList.add(json);
            }
        }
        orderList.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                int orderNumber1 = o1.getInteger("orderNumber");
                int orderNumber2 = o2.getInteger("orderNumber");
                return orderNumber1 - orderNumber2;
            }
        });
        return orderList;
    }
}
