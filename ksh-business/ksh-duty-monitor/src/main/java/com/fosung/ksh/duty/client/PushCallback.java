package com.fosung.ksh.duty.client;

import afu.org.checkerframework.checker.units.qual.A;
import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutyRecord;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyRecordService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.dto.PersonRecordInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Date;

import static com.fosung.ksh.duty.task.DutyRecordTask.isAm;

/**  
 * 发布消息的回调类  
 *   
 * 必须实现MqttCallback的接口并实现对应的相关接口方法  
 *      ◦CallBack 类将实现 MqttCallBack。每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。在回调中，将它用来标识已经启动了该回调的哪个实例。  
 *  ◦必须在回调类中实现三个方法：  
 *   
 *  public void messageArrived(MqttTopic topic, MqttMessage message)  
 *  接收已经预订的发布。  
 *   
 *  public void connectionLost(Throwable cause)  
 *  在断开连接时调用。  
 *   
 *  public void deliveryComplete(MqttDeliveryToken token))  
 *      接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。  
 *  ◦由 MqttClient.connect 激活此回调。  
 *   
 */
@Slf4j
@Component
public class PushCallback extends ApplicationObjectSupport implements MqttCallback {
   @Autowired
	private DutyRecordService recordService;
	@Autowired
	private DutyPeopleService peopleService;
	@Autowired
	private DutyVillageRecordService villageRecordService;
	private  HkiMQClient hkiMQClient;

  
    public void connectionLost(Throwable cause) {
		log.warn("MQ连接丢失！并尝试重新连接！");
		hkiMQClient.startReconnect();

    }  
  
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
	             JSONObject jsonObject=JsonMapper.parseObject(new String(message.getPayload()));
		PersonRecordInfo recordInfo= JsonMapper.parseObject(jsonObject.get("alarmExt").toString(), PersonRecordInfo.class);
			for (PersonInfo human : recordInfo.getHumans()) {
				try {
					String alarmId = recordInfo.getAlarmId();
					String humanId = human.getHumanId();
					// 第二步：验证当前签到人员是否为值班人员
					DutyPeople people = peopleService.getByHumanId(humanId);
					if (people != null) {
						// 第三步：验证当前签到记录是否已保存
						DutyRecord record = recordService.getByAlarmId(alarmId, humanId);
						if (record == null) {
							record = new DutyRecord();
							record.setHumanId(people.getHumanId());
							record.setDutyPeopleId(people.getId().toString());
							record.setDutySignTime(recordInfo.getAlarmTime());
							record.setIndexCode(recordInfo.getIndexCode());
							record.setAlarmId(alarmId);
							recordService.save(record);

							// 第四步：验证本村今日的签到记录是否已经保存
							DutyVillageRecord villageRecord = villageRecordService.getByVillageIdAll(people.getVillageId(), recordInfo.getAlarmTime());

							if (villageRecord != null) {
								// 判断上午签到时间是否存在
								if (villageRecord.getAmSignTime() == null){
									// 添加上午签到时间
									villageRecord.setDutyPeopleId(people.getId() + "");
									villageRecord.setAmSignTime(recordInfo.getAlarmTime());
									villageRecord.setIndexCode(recordInfo.getIndexCode());
									villageRecordService.update(villageRecord,
											Sets.newHashSet("dutyPeopleId",
													"amSignTime",
													"indexCode"));

								}else {
									// 判断签到时间是否在12点之前
									if (isAm(recordInfo.getAlarmTime())){
										// 判断上午第二次签到时间是否大于第一次
										if (recordInfo.getAlarmTime().getTime()>villageRecord.getAmSignTime().getTime()){
											// 添加上午签退时间
											villageRecord.setAmSignOffTime(recordInfo.getAlarmTime());
											villageRecordService.update(villageRecord, Sets.newHashSet("amSignOffTime"));
										}else {
											// 添加上午签退时间
											villageRecord.setAmSignOffTime(villageRecord.getAmSignTime());
											villageRecord.setAmSignTime(recordInfo.getAlarmTime());
											villageRecordService.update(villageRecord, Sets.newHashSet("amSignOffTime","amSignTime"));
										}
									}else {
										// 判断下午签到时间是否存在
										if (villageRecord.getPmSignTime() == null){
											// 添加下午签到时间
											villageRecord.setDutyPmPeopleId(people.getId() + "");
											villageRecord.setPmSignTime(recordInfo.getAlarmTime());
											villageRecordService.update(villageRecord,
													Sets.newHashSet("dutyPmPeopleId", "pmSignTime"));
										} else {
											// 添加下午签退时间
											villageRecord.setPmSignOffTime(recordInfo.getAlarmTime());
											villageRecordService.update(villageRecord, Sets.newHashSet( "pmSignOffTime"));
										}

									}
								}
							}
//                                    }
						}
					}
				} catch (Exception e) {
					log.error("签到记录保存失败：失败的人员：\nrecordInfo={},\nhuman={},\n错误信息：{}",
							JsonMapper.toJSONString(recordInfo, true),
							JsonMapper.toJSONString(human, true),
							ExceptionUtils.getStackTrace(e));
				}

			}





	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println(new Date() + "Server:2:" + "deliveryComplete---------"+ token.isComplete() + token.getMessageId());
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}


}