package com.fosung.ksh.organization.controller;

import com.alibaba.fastjson.JSON;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.organization.constant.PushStatus;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.ezb.dto.ClassificationDTO;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.fosung.ksh.organization.util.QrCodeCreateUtil;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyh
 */
@Api(description = "组织生活相关接口，并和E支部进行同步",
        tags = {"1、组织生活接口"},
        produces = "wangyihua")
@RestController
@RequestMapping(value = OrgLifeController.BASE_PATH)
public class OrgLifeController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/org-life";
    private static final String REDIS_TEMP_DROP = "drop:";
    @Value("${app.visual.drop-url:}")
    private String dropMobileHost;
    @Autowired
    private OrgLifeService orgLifeService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 自定义组织生活分页查询
     *
     * @param classificationId 组织生活类型
     * @param startDate        开始时间
     * @param endDate          结束时间
     * @param pushStatus       发布状态
     * @param meetingName      组织生活标题
     * @param branchId         党支部ID
     * @param orgId            党组织ID
     * @param pageRequest      分页
     * @return
     */
    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<OrgLife>> queryPageList(
            @ApiParam("组织生活类型ID") @RequestParam(required = false) String classificationId,
            @ApiParam("开始时间") @DateTimeFormat(pattern = AppProperties.DATE_PATTERN) @RequestParam(required = false) Date startDate,
            @ApiParam("结束时间") @DateTimeFormat(pattern = AppProperties.DATE_PATTERN) @RequestParam(required = false) Date endDate,
            @ApiParam("发布状态") @RequestParam(required = false) PushStatus pushStatus,
            @ApiParam("标题") @RequestParam(required = false) String meetingName,
            @ApiParam("组织ID") @RequestParam String orgId,
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        Page<OrgLife> page = orgLifeService.queryPageList(classificationId,
                startDate,
                endDate,
                pushStatus,
                meetingName,
                orgId,
                pageNum,
                pageSize);
        return Result.success(page);
    }

    /**
     * 获取组织生活详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取组织生活详情")
    @RequestMapping(value = "classification", method = RequestMethod.POST)
    public ResponseEntity<List<ClassificationDTO>> queryClassificationList(
            @ApiParam("组织CODE") @RequestParam String orgCode) throws IOException {
        return Result.success(orgLifeService.queryClassificationList(orgCode));
    }

    /**
     * 获取组织生活详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取组织生活详情")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ResponseEntity<OrgLife> getInfo(@ApiParam("组织生活ID") @RequestParam Long id) {
        return Result.success(orgLifeService.getInfo(id));
    }

    /**
     * 自定义保存
     *
     * @param orgLife
     */
    @ApiOperation(value = "保存或修改数据")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<OrgLife> customSave(@RequestBody OrgLife orgLife) throws Exception {
        return Result.success(orgLifeService.customSave(orgLife));
    }


    /**
     * 自定义保存
     *
     * @param orgLife
     */
    @ApiOperation(value = "结束组织生活，并上传附件")
    @RequestMapping(value = "finish", method = RequestMethod.POST)
    public ResponseEntity<OrgLife> finish(@RequestBody OrgLife orgLife) throws Exception {
        return Result.success(orgLifeService.finish(orgLife));
    }

    /**
     * 组织生活发布
     *
     * @param id
     */
    @ApiOperation(value = "组织生活发布")
    @RequestMapping(value = "push", method = RequestMethod.POST)
    public void push(@ApiParam("组织生活ID") @RequestParam Long id) throws InterruptedException, ExecutionException, IOException {
        orgLifeService.push(id);
    }



    /**
     * 二维码
     */
    @GetMapping("/qrcode")
    public ResponseParam getQRCode(@RequestParam(required = false, defaultValue = "#{T(java.util.UUID).randomUUID().toString()}") String dropId,
//                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String orgCode,
                                   @RequestParam(required = false) String orgId) {
        String qrCodeContent = dropMobileHost + "?dropId=" + dropId;
//        if (id != null) {
//            qrCodeContent = qrCodeContent + "&id=" + id;
//        }
        if (!Strings.isNullOrEmpty(orgId)){
            qrCodeContent = qrCodeContent + "&orgId=" + orgId;
        }
        if (!Strings.isNullOrEmpty(orgCode)){
            qrCodeContent = qrCodeContent + "&orgCode=" + orgCode;
        }
        System.out.println(qrCodeContent);
        String imageBase64 = "data:image/jpeg;base64," + QrCodeCreateUtil.createQrCode(qrCodeContent, 300, "jpg");
        Map<String, String> map = new HashMap<>();
        map.put("dropId", dropId);
        map.put("image", imageBase64);
        return ResponseParam.saveSuccess().data(map);
    }

    @ApiOperation(value = "手机端保存或修改数据")
    @RequestMapping(value = "mobleSave", method = RequestMethod.POST)
    public ResponseEntity<OrgLife> mobleSave(@RequestBody OrgLife orgLife) throws Exception {
        System.out.println(JSON.toJSONString(orgLife));
        String dropId = orgLife.getDropId();
        if (Strings.isNullOrEmpty(dropId)) {
            return Result.failure("请提交DropId");
        }
        redisTemplate.opsForValue().set(REDIS_TEMP_DROP + dropId, JSON.toJSONString(orgLife), 1L, TimeUnit.DAYS);
        return Result.success(orgLife);
    }

    @GetMapping("/drop/get")
    public ResponseEntity<OrgLife>  getDropData(@NotBlank @RequestParam String dropId) {
        String dataStr = redisTemplate.opsForValue().get(REDIS_TEMP_DROP + dropId);
        OrgLife orgLife = Strings.isNullOrEmpty(dataStr) ? null : JSON.parseObject(dataStr, OrgLife.class);
        return Result.success(orgLife);
    }

}
