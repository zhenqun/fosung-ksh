package com.fosung.ksh.meeting.control.hst.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:组织返回信息
 * @auther: xingxl
 * @date: 2018/12/4 15:35
 */
@Data
public class DepartResponseDTO implements Serializable {
    private static final long serialVersionUID = -1228032683418980378L;

    /*
     * 暂时未使用
     */
    private Integer backupDepartId;
    /*
     * 组织描述
     */
    private String departDesc;
    /*
     * 本组织的id
     */
    private Integer departId;
    /*
     * 部门级别 0普通部门 1中级部门 2高级部门  仅供参考
     */
    private String departLevel;
    /*
     * 组织名称
     */
    private String departName;
    /*
     * 0不对上级节点显示 1可以对上级节点显示
     */
    private String departType;
    /*
     * 是否是最后一个组织 1是 0不是
     */
    private String isFinalDepart;
    /*
     * 所属节点ID，节点与服务器节点相同则代表数据出自于此服务器
     */
    private Integer nodeId;
    /*
     * 上级组织的id
     */
    private Integer parentDepartId;
}
