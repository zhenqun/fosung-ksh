package com.fosung.ksh.swgk.vo;

import java.util.Date;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 便民服务信息
 * 
 * @author tz
 * 
 */
@Data
public class Convenience {
	private String id;
	private String title;
	private String content;
	private String image;
	// 上传附件url
	private String url;
	private String areaId;
	private Integer isTop;
	// 排序id 越大越靠前
	private Integer sortId;
	private Date addTime;
	private String addMan;
	private Date updateTime;
	private String updateMan;
	private Integer deleteFlag;
	private Date deleteTime;
	private String deleteMan;
	private Date endTime;
	private MultipartFile attach;
	private String areaName;
	private String orgCode;
	private String orgId;


	

}
