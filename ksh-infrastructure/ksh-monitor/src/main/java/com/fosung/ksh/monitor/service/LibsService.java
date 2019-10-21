package com.fosung.ksh.monitor.service;

public interface LibsService {
    /**
     * 创建人脸库接口
     *
     * @param listLibName 人脸库名称
     * @param describe    描述
     * @return
     */
    public String addLibs(String listLibName, String describe);

    /**
     * 修改人脸库接口
     *
     * @param listLibName 人脸库名称
     * @param describe    描述
     * @return
     */
    public void modifyLibs(String listLibId, String listLibName, String describe);


    /**
     * 删除人脸库接口
     *
     * @return
     */
    public void deleteLibs(String listLibId);

}
