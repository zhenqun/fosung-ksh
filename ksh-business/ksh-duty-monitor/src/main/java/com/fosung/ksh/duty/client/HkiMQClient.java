package com.fosung.ksh.duty.client;

import com.fosung.framework.common.util.UtilString;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix ="topic")
@Component
public class HkiMQClient {
    @Autowired
    PushCallback pushCallback;
    public final static  String HOST ="tcp://123.130.115.76:1883";// "tcp://127.0.0.1:1883";
    public final static String TOPIC = "fas/admin";//第三方接收时，topic名称调用API网关接口获得

    private static final String clientid = "admin";//必须唯一，不能重复，否则会把前一个踢掉
    private MqttClient client;  
    private MqttConnectOptions options;
    @Setter
    private String userName = "";  //第三方接收时，用户名、密码是API网关定的
   @Setter
    private String passWord = "";
    private ScheduledExecutorService scheduler;
    private  static HkiMQClient hkiMQClient;

  /* public  static HkiMQClient getInstance(String userName, String passWord){
           if(UtilString.isNotBlank(userName)&&UtilString.isNotBlank(passWord)){
               hkiMQClient.setPassWord(passWord);
               hkiMQClient.setUserName(userName);
           }
        return  hkiMQClient;
   }*/
 
    public void start() {
        try {  
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存  
            client = new MqttClient(HOST, clientid, new MemoryPersistence());  
            // MQTT的连接设置  
            options = new MqttConnectOptions();  
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接  
            //true-连接不保持，false-连接保持
            options.setCleanSession(false);  
            // 设置连接的用户名  
            options.setUserName(userName);  
            // 设置连接的密码  
            options.setPassword(passWord.toCharArray());  
            // 设置超时时间 单位为秒  
            options.setConnectionTimeout(15);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制  
            options.setKeepAliveInterval(5);  
            // 设置回调  
            client.setCallback(pushCallback);
            MqttTopic topic = client.getTopic("client down");  
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息    
            options.setWill(topic, "close".getBytes(), 0, true);  

            client.connect(options);  
            //订阅消息
            int[] Qos  = {1};  
            String[] topic1 = {TOPIC};  
            client.subscribe(topic1, Qos);  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  //重新链接
    public void startReconnect() {  
        scheduler = Executors.newSingleThreadScheduledExecutor();  
        scheduler.scheduleAtFixedRate(new Runnable() {  
            public void run() {  
                if (!client.isConnected()) {  
                    try {  
                        client.connect(options);  
                    } catch (MqttSecurityException e) {  
                        e.printStackTrace();  
                    } catch (MqttException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);  
    } 
    
    public void disconnect() {  
        try {  
           client.disconnect();  
       } catch (MqttException e) {  
           e.printStackTrace();  
       }  
   }  
  
}  