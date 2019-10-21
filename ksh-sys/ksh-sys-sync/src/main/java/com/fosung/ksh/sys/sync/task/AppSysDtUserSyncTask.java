package com.fosung.ksh.sys.sync.task;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.common.cache.CommonRedisLock;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.SysDtUser;
import com.fosung.ksh.sys.service.SysDtUserService;
import com.fosung.ksh.sys.sync.support.AppSysDtUserSupport;
import com.fosung.ksh.sys.sync.support.AppSysOrgSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyihua
 * @date 2019-06-07 14:58
 */
@Slf4j
@Component
public class AppSysDtUserSyncTask {
    /**
     * redis锁前缀
     */
    private static final String PREFIX = "LOCK_DT_USER_";

    /**
     * 默认锁住5小时
     */
    private static final Long LOCK_EXPIRE = 1000L * 60 * 60 * 5;

    @Autowired
    private AppSysDtUserSupport sysDtUserSupport;

    @Autowired
    private SysDtUserService sysDtUserService;
    @Autowired
    private SysProperties sysProperties;

    @Autowired
    private AppSysOrgSupport sysOrgSupport;

    @Autowired
    private CommonRedisLock commonRedisLock;


    @Scheduled(cron = "0 10 01 * * ?")
    public void execute() {
        String orgId = sysProperties.getRootOrgId();
        List<SysOrg> sysOrgList = sysOrgSupport.queryOrgList(orgId);
        for (SysOrg sysOrg : sysOrgList) {
            boolean lock = commonRedisLock.lock(PREFIX + sysOrg.getOrgId(), LOCK_EXPIRE);
            if (lock) {
                try {
                    List<SysOrg> sysOrg2List = sysOrgSupport.queryOrgList(sysOrg.getOrgId());
                    for (SysOrg org : sysOrg2List) {
                        List<SysOrg> branchList = sysOrgSupport.queryBranchList(org.getOrgId());
                        for (SysOrg branch : branchList) {
                            sync(branch.getOrgId());
                        }

                    }
                } catch (Exception e) {
                    log.error("党组织{}下用户同步失败，{}", sysOrg.getOrgId(), e.getMessage());
                    e.printStackTrace();
                    commonRedisLock.delete(PREFIX + sysOrg.getOrgId());
                }

            }
        }
        log.info("党员同步结束");
    }


    /**
     * 数据执行同步操作
     *
     * @param orgId
     */
    public void sync(String orgId) {
        List<SysDtUser> addList = Lists.newArrayList();
        List<SysDtUser> updateList = Lists.newArrayList();
        List<SysDtUser> deleteList = Lists.newArrayList();


        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("orgId", orgId);
        // 本地数据
        List<SysDtUser> userDBList = sysDtUserService.queryAll(searchParam);

        // 简项库数据
        List<SysDtUser> userRTList = sysDtUserSupport.queryUserAllByOrgId(orgId);


        Map<String, SysDtUser> dbMap = userDBList.stream().collect(Collectors.toMap(SysDtUser::getUserId, sysDtUser -> sysDtUser));
        Map<String, SysDtUser> rtMap = userRTList.stream().collect(Collectors.toMap(SysDtUser::getUserId, sysDtUser -> sysDtUser));

        for (SysDtUser rt : userRTList) {

            // 验证对象是新增修改或者删除数据
            SysDtUser db = dbMap.get(rt.getUserId());
            if (db == null) {
                rt.setIsUse(true);
                addList.add(rt);
            } else if (!compareTo(rt, db)) {
                updateList.add(rt);
            }
        }

        // 批量新增
        sysDtUserService.saveBatch(addList);

        // 批量修改
        for (SysDtUser sysDtUser : updateList) {
            update(sysDtUser);
        }


        // 被删除的数据
        deleteList = userDBList.stream().filter(sysDtUser ->
                sysDtUser.getIsUse()
                        && !rtMap.containsKey(sysDtUser.getUserId())).collect(Collectors.toList());


        // 处理未进行修改的数据
        if (UtilCollection.isNotEmpty(deleteList)) {
            List<String> deleteIdList = Lists.newArrayList();
            deleteList.stream().forEach(sysDtUser -> {
                deleteIdList.add(sysDtUser.getUserId());
            });

            sysDtUserService.batchCloseUse(deleteIdList, new Date());
        }
        log.info("\nSysDtUser同步结果：\n" +
                        "党支部orgId：{},\n" +
                        "远程同步数据{}条，\n" +
                        "数据库中数据{}条，\n" +
                        "新增{}条，\n" +
                        "修改{}条，\n" +
                        "删除{}条\n" +
                        "未修改{}条",
                orgId,
                userRTList.size(),
                userDBList.size(),
                addList.size(),
                updateList.size(),
                deleteList.size(),
                userDBList.size() - updateList.size() - deleteList.size());
    }


    /**
     * 修改数据
     *
     * @param sysDtUser
     */
    private void update(SysDtUser sysDtUser) {
        Set<String> updateFields = Sets.newHashSet("org_id",
                "orgName",
                "orgCode",
                "hash",
                "userName",
                "telephone",
                "idCard",
                "realName",
                "isUse");
        sysDtUser.setIsUse(true);
        if (sysDtUser.getCreateDatetime() != null) {
            updateFields.add("createDatetime");
        }
        sysDtUserService.update(sysDtUser, updateFields);
    }


    /**
     * 比较远程对象和数据库中对象是否一致
     *
     * @param rt
     * @param db
     * @return
     */
    private Boolean compareTo(SysDtUser rt, SysDtUser db) {
        Boolean compare = true;

        // 验证数据是否一致
        compare = rt.equals(db);
        rt.setIsUse(true);
        rt.setId(db.getId());
        if (!db.getIsUse()) {
            compare = false;
            rt.setCreateDatetime(new Date());
        }

        return compare;
    }

}
