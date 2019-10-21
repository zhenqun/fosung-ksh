package com.fosung.ksh.sys.sync.task;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.ksh.common.cache.CommonRedisLock;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.service.SysOrgService;
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
public class AppSysOrgSyncTask {

//
//    /**
//     * redis锁前缀
//     */
//    private static final String PREFIX = "LOCK_ORG_";
//    private static final Long LOCK_EXPIRE = 1000L * 60 * 60 * 5; // 默认锁住5小时
//
//    @Autowired
//    private SysProperties sysProperties;
//
//    @Autowired
//    private AppSysOrgSupport sysOrgSupport;
//
//    @Autowired
//    private CommonRedisLock commonRedisLock;
//
//    @Autowired
//    private SysOrgService sysOrgService;
//
//
//    @Scheduled(cron = "0 10 0 * * ?")
//    public void execute() {
//        String orgId = sysProperties.getRootOrgId();
//        List<SysOrg> sysOrgList = sysOrgSupport.queryOrgList(orgId);
//        for (SysOrg sysOrg : sysOrgList) {
//            boolean lock = commonRedisLock.lock(PREFIX + sysOrg.getOrgId(), LOCK_EXPIRE);
//            if (lock) {
//                try {
//                    sync(sysOrg.getOrgId());
//                } catch (Exception e) {
//                    log.error("党组织{}下用户同步失败，{}", sysOrg.getOrgId(), e.getMessage());
//                    e.printStackTrace();
//                    commonRedisLock.delete(PREFIX + sysOrg.getOrgId());
//                }
//            }
//        }
//        log.info("党组织同步结束");
//    }
//
//
//    /**
//     * 数据执行同步操作
//     *
//     * @param orgId
//     */
//    public void sync(String orgId) {
//
//
//        List<SysOrg> addList = Lists.newArrayList();
//        List<SysOrg> updateList = Lists.newArrayList();
//        List<SysOrg> deleteList = Lists.newArrayList();
//
//
//        Map<String, Object> searchParam = Maps.newHashMap();
//        searchParam.put("parentId", orgId);
//        // 本地数据
//        List<SysOrg> orgDBList = sysOrgService.queryAll(searchParam);
//        // 简项库数据
//        List<SysOrg> orgRTList = sysOrgSupport.queryOrgList(orgId);
//
//        Map<String, SysOrg> dbMap = orgDBList.stream().collect(Collectors.toMap(SysOrg::getOrgId, sysOrg -> sysOrg));
//        Map<String, SysOrg> rtMap = orgRTList.stream().collect(Collectors.toMap(SysOrg::getOrgId, sysOrg -> sysOrg));
//
//        for (SysOrg rt : orgRTList) {
//            // 验证对象是新增修改或者删除数据
//            SysOrg db = dbMap.get(rt.getOrgId());
//            if (db == null) {
//                rt.setIsUse(true);
//                addList.add(rt);
//            } else if (!compareTo(rt, db)) {
//                updateList.add(rt);
//            }
//        }
//
//        // 被删除的数据
//        deleteList = orgDBList.stream().filter(sysOrg ->
//                sysOrg.getIsUse()
//                        && !rtMap.containsKey(sysOrg.getOrgId())).collect(Collectors.toList());
//
//        // 批量新增
//        sysOrgService.saveBatch(addList);
//
//        // 批量修改
//        for (SysOrg sysOrg : updateList) {
//            Set<String> updateFields = Sets.newHashSet("orgName", "orgCode", "parentId",
//                    "orderNumber", "hasChildren", "isUse");
//            sysOrg.setIsUse(true);
//            if (sysOrg.getCreateDatetime() != null) {
//                updateFields.add("createDatetime");
//            }
//            sysOrgService.update(sysOrg, updateFields);
//        }
//
//        // 处理未进行修改的数据
//        if (UtilCollection.isNotEmpty(deleteList)) {
//            List<String> deleteIdList = Lists.newArrayList();
//            deleteList.stream().forEach(sysOrg -> {
//                deleteIdList.add(sysOrg.getOrgId());
//            });
//
//            sysOrgService.batchCloseUse(deleteIdList, new Date());
//        }
//        log.info("\nSysOrg同步结果：\n" +
//                        "党支部orgId：{},\n" +
//                        "远程同步数据{}条，\n" +
//                        "数据库中数据{}条，\n" +
//                        "新增{}条，\n" +
//                        "修改{}条，\n" +
//                        "删除{}条\n" +
//                        "未修改{}条",
//                orgId,
//                orgRTList.size(),
//                orgDBList.size(),
//                addList.size(),
//                updateList.size(),
//                deleteList.size(),
//                orgDBList.size() - updateList.size() - deleteList.size());
//
//        // 递归循环查询
//        orgRTList.stream().filter(sysOrg -> sysOrg.getHasChildren()).forEach(sysOrg -> {
//            sync(sysOrg.getOrgId());
//        });
//
//    }
//
//
//    /**
//     * 比较远程对象和数据库中对象是否一致
//     *
//     * @param rt
//     * @param db
//     * @return
//     */
//    private Boolean compareTo(SysOrg rt, SysOrg db) {
//        Boolean compare = true;
//
//        // 验证数据是否一致
//        compare = db.getOrgId().equals(rt.getOrgId())
//                && db.getOrgCode().equals(rt.getOrgCode())
//                && db.getOrgName().equals(rt.getOrgName())
//                && db.getParentId().equals(rt.getParentId())
//                && db.getHasChildren() == rt.getHasChildren();
//
////        rt.setIsUse(true);
////        rt.setId(db.getId());
////        if (!db.getIsUse()
////                && !UtilDate.getDateFormatStr(db.getLastUpdateDatetime(), AppProperties.DATE_PATTERN).equals(UtilDate.getDateFormatStr(new Date(), AppProperties.DATE_PATTERN))) {
////            compare = false;
////            rt.setCreateDatetime(new Date());
////        }
//
//        return compare;
//    }

}
