package com.fosung.ksh.swgk.vo;

import java.util.Date;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
/**
 * 三务公开文章信息详情
 * @author tz
 *
 */
@Data
public class ArticleDetails {
	private String id;		
	private String title;		// 标题
	private String content;		// 内容
	private String image;		// 图片路径
	private String url;			//	附件
	private String fileName;
	private Integer	isTop;			// 置顶
	private Integer sort;			// 排序
	private String cateId;		// 所属分类
	private Date addTime;		// 添加时间
	private String AddMan;		//	添加人
	private Date updateTime;	//	更新时间
	private String updateMan; 	// 更新人
	private Integer deleteFlag;
	private Date deleteTime;
	private String deleteMan;
	private Integer state;
	private String areaId; 		// 地区
	private MultipartFile attach;		// 上传的附件
	private Date scheduleTime;  //延时发布时间
	private Integer hits;//点击次数
	private String orgCode;
	private String orgId;
	
}
