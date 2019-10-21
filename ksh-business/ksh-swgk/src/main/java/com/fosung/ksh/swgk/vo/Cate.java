package com.fosung.ksh.swgk.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文章分类（三务公开菜单）
 * 
 * @author tz
 * 
 */
@Data
public class Cate {
	// 三务公开目录表ID
	private String id;
	private String cateName;
	private String cateDesc;
	private Date addTime;
	private String addMan;
	private Date updateTime;
	private String updateMan;
	private Integer deleteFlag;
	private Date deleteTime;
	private String deleteMan;
	private String areaId;
	private String parentId;
	// 0:社区三务 1：农村三务
	private Integer type;
	private Integer ctype;
	private Integer isShow;		// 是否公开显示
	private Integer sortId;
	private boolean hasChildren;
	private List<Cate> children;
	private Integer level;		// 表单中的父级目录
	
	private MultipartFile file;
	private String attachId;
//	private Attach attach;
	private ArticleSimple article;
	private String orgCode;
	private String orgId;


	

	

	

}
