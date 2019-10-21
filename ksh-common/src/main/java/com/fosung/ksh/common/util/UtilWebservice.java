package com.fosung.ksh.common.util;

import com.fosung.framework.common.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import java.net.URL;

/**
 * 调用webservice工具类
 *
 * @author wangyihua
 * @
 */
@Slf4j
public class UtilWebservice {


    /**
     * 调用Webservice接口，并返回调用结果
     *
     * @param wsUrl  ws服务地址
     * @param method 调用的方法
     * @param args   传递的参数
     * @return 返回调用结果
     */
    public static JSON execute(String wsUrl, String method, Object[] args) throws Exception {

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(new URL(wsUrl));

        Object[] res = client.invoke(method, args);

        String result = (String) res[0];

        log.info("\n请求地址：{},\n方法：{},\n参数: {}, \n返回结果：{}",
                wsUrl,
                method,
                JsonMapper.toJSONString(args,true),
                result);

        return parse(result);

    }


    /**
     * 解析返回值,返回json类型
     *
     * @param result
     * @return
     */
    private static JSON parse(String result) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(result);
        return json;
    }
}
