package com.fosung.ksh.punsh.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @date 2019-3-28 14:52
 */
@Entity
@Table(name="client_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo extends AppJpaBaseEntity {

    
    private String csds;
    
    /**
     *客户端的序列号
     */
    private String sn;
    
    /**
     *客户端支持的语言
     */
    private String language;
    
    /**
     *获取服务器配置参数,目前只有all
     */
    private String options;

    /**
     *push协议的版本
     */
    private String pushver;
    
    /**
     *客户端ip
     */
    private String host;

    /**
     * 客户端端口号
     */
    private int port;

    /**
     * 行政机构code
     */
    @Column(name = "org_code")
    private String orgCode;

}
