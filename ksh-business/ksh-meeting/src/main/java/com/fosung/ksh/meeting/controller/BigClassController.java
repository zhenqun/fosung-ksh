package com.fosung.ksh.meeting.controller;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.entity.BigClass;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.service.BigClassService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.gson.JsonArray;
import io.swagger.annotations.ApiParam;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.util.UtilString;

@RestController
@RequestMapping(value=BigClassController.BASE_PATH)
public class BigClassController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/big_class/" ;

    @Autowired
    private BigClassService bigClassService ;
    @Autowired
    private SysOrgClient sysOrgClient;

    /**
     * 记录分页查询
     * @param pageNum 分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @PostMapping("query")
    public ResponseEntity<Page<BigClass>> query(
            @RequestParam(required=false , value="pagenum" , defaultValue=""+DEFAULT_PAGE_START_NUM)int pageNum ,
            @RequestParam(required=false , value="pagesize" , defaultValue=""+DEFAULT_PAGE_SIZE_NUM)int pageSize){
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith( getHttpServletRequest(),
                DEFAULT_SEARCH_PREFIX ) ;
        //执行分页查询
        Page<BigClass> bigClassPage = bigClassService.queryByPage(searchParam , pageNum , pageSize,new String[]{"createDatetime_desc"}) ;

        DtoUtil.handler(bigClassPage.getContent(), getDtoCallbackHandler());
        return Result.success(bigClassPage);
    }
    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @PostMapping("get")
    public ResponseEntity<BigClass> detail(@RequestParam Long id){
        //查询BigClass
        BigClass bigClass = bigClassService.get(id) ;
        return Result.success(bigClass);
    }
    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     * @param bigClass
     * @return
     * @throws Exception
     */
    @PostMapping("save")
    public ResponseEntity<BigClass> save(@Valid @RequestBody BigClass bigClass) {
        //id不为空，进行更新操作，否则进行添加
        if(bigClass.getId() != null){
			//由请求参数中获取需要更新的字段
			Set<String> updateFields = Sets.newLinkedHashSet("title", "vadioId", "imageId","orgName","orgCode","files","images");
			//按照字段更新对象"
            String userHash=bigClass.getUserHash();
            bigClass.setCreateUserId(userHash);
            JSONArray vadio=bigClass.getFiles();
            if (vadio.size()>0){
                JSONObject obj=vadio.getJSONObject(0);
                String vadioId=obj.getString("uid");
                bigClass.setVadioId(vadioId);
            }
            JSONArray images= bigClass.getImages();
            if (images.size()>0){
                JSONObject obj=images.getJSONObject(0);
                String imageId=obj.getString("uid");
                bigClass.setImageId(imageId);
            }
			bigClassService.update( bigClass , updateFields ) ;

			return Result.success(bigClass);
        }else{
            String userHash=bigClass.getUserHash();
            bigClass.setCreateUserId(userHash);
            JSONArray vadio=bigClass.getFiles();
            if (vadio.size()>0){
                JSONObject obj=vadio.getJSONObject(0);
                String vadioId=obj.getString("uid");
                bigClass.setVadioId(vadioId);
            }
            JSONArray images= bigClass.getImages();
            if (images.size()>0){
                JSONObject obj=images.getJSONObject(0);
                String imageId=obj.getString("uid");
                bigClass.setImageId(imageId);
            }
			bigClassService.save(bigClass);
			return Result.success(bigClass);
        }
    }

    /**
     * 删除信息
     * @param ids
     * @return
     */

    @PostMapping("delete")
    public ResponseEntity delete(@RequestParam(value = "id") Long id){
        bigClassService.delete(id);
        return Result.success();
    }
    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<BigClass> dtoCallbackHandler = new KshDTOCallbackHandler<BigClass>() {
            @Override
            public void doHandler(BigClass dto) {
                Date createTime=dto.getCreateDatetime();
                SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time= f.format(createTime);
                dto.setCreateTime(time);
                SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
                if (sysOrg != null) {
                    dto.setOrgCode(sysOrg.getOrgCode());
                    dto.setOrgName(sysOrg.getOrgName());

                }
            }
        };
        return dtoCallbackHandler;
    }



}
