package com.fosung.ksh.monitor.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AlarmTopicInfo {
     private  String  ip;
     private Map<String,String> topic;
}
