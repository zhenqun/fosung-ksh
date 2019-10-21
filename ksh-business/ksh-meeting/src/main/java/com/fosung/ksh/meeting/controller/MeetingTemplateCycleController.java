package com.fosung.ksh.meeting.controller;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import com.fosung.ksh.meeting.service.MeetingTemplateCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.util.UtilString;

@RestController
@RequestMapping(value=MeetingTemplateCycleController.BASE_PATH)
public class MeetingTemplateCycleController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meetingtemplatecycle" ;

    @Autowired
    private MeetingTemplateCycleService meetingTemplateCycleService ;

    /**
     * 记录分页查询
     * @param pageNum 分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @PostMapping("query")
    public ResponseParam query(
            @RequestParam(required=false , value="pagenum" , defaultValue=""+DEFAULT_PAGE_START_NUM)int pageNum ,
            @RequestParam(required=false , value="pagesize" , defaultValue=""+DEFAULT_PAGE_SIZE_NUM)int pageSize){
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith( getHttpServletRequest(),
                DEFAULT_SEARCH_PREFIX ) ;
        //执行分页查询
        Page<MeetingTemplateCycle> meetingTemplateCyclePage = meetingTemplateCycleService.queryByPage(searchParam , pageNum , pageSize) ;

        //数据库实体与前端展示字段之间的转换，需要指定前端展示需要的字段
        List<Map<String, Object>> meetingTemplateCycleList = UtilDTO.toDTO(meetingTemplateCyclePage.getContent(),
                null , getDtoCallbackHandler()) ;

        return ResponseParam.success()
                .pageParam( meetingTemplateCyclePage )
                .datalist( meetingTemplateCycleList ) ;
    }
    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @PostMapping("get")
    public ResponseParam detail(@RequestParam Long id){
        //查询模板循环
        MeetingTemplateCycle meetingTemplateCycle = meetingTemplateCycleService.get(id) ;

        //将实体转换为数据传输对象
        Map<String, Object> dtoObject = UtilDTO.toDTO( meetingTemplateCycle ,null , getDtoCallbackHandler() ) ;

        return ResponseParam.success()
                .data( dtoObject );
    }
    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     * @param meetingTemplateCycle
     * @return
     * @throws Exception
     */
    @PostMapping("save")
    public ResponseParam save(@Valid MeetingTemplateCycle meetingTemplateCycle) {
        //id不为空，进行更新操作，否则进行添加
        if(meetingTemplateCycle.getId() != null){
			//由请求参数中获取需要更新的字段
			Set<String> updateFields = getHttpServletRequest().getParameterMap().keySet() ;
			//按照字段更新对象
			meetingTemplateCycleService.update( meetingTemplateCycle , updateFields ) ;

			return ResponseParam.updateSuccess() ;
        }else{
			meetingTemplateCycleService.save(meetingTemplateCycle);
			return ResponseParam.saveSuccess() ;
        }
    }

    /**
     * 删除信息
     * @param ids
     * @return
     */
    @PostMapping("delete")
    public ResponseParam delete(@RequestParam(required=true , value="ids") String ids) {
        if( UtilString.isBlank(ids) ){
			return ResponseParam.deleteFail() ;
        }
        //执行删除
        meetingTemplateCycleService.delete( toLongIds( ids ) ) ;

        return ResponseParam.deleteSuccess() ;
    }

    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     * @return
     */
    public DTOCallbackHandler getDtoCallbackHandler() {

        //创建转换接口类
        DTOCallbackHandler dtoCallbackHandler = new DTOCallbackHandler() {
            @Override
            public void doHandler(Map<String, Object> dtoMap, Class<?> itemClass) {

            }
        };

        return getDTOCallbackHandlerProxy(dtoCallbackHandler,true);
    }

}