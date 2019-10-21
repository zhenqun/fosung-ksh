package com.fosung.ksh.sys.handler;

import com.fosung.ksh.sys.entity.SysUser;

public interface UserHandler {

    /**
     * 通过hash获取用户详细信息
     *
     * @param hash
     * @return
     */
    public SysUser getUserByHash(String hash);


    /**
     * 通过hash获取用户减项信息
     *
     * @param hash
     * @return
     */
    public SysUser getSimpleUserByHash(String hash);

}
