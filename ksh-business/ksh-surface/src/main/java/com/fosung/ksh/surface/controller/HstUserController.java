package com.fosung.ksh.surface.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.surface.dto.SysUserDTO;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author wangyihua
 * @date 2019-05-07 13:03
 */
@Slf4j
@Api(description = "福生可视化对外提供接口服务", tags = {"1、会议室接口"})
@RestController
public class HstUserController extends AppIBaseController {

    @Autowired
    private SysUserClient sysUserClient;

    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "通过hash获取用户信息")
    @RequestMapping(value = "hst/user/info/hash", method = RequestMethod.POST)
    public ResponseParam userInfo(
            @ApiParam("用户HASH")
            @RequestParam String userHash) throws UnsupportedEncodingException {
        log.info("userHash=" + userHash);
//        userHash = URLDecoder.decode(userHash,"UTF-8");
//        log.info("userHash转换后=" + userHash);
        SysUser sysUser = sysUserClient.getUserInfo(userHash);
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUserHash(sysUser.getHash());
        sysUserDTO.setName(sysUser.getRealName());
        sysUserDTO.setOrgId(sysUser.getOrgId());
        sysUserDTO.setOrgCode(sysUser.getOrgCode());
        sysUserDTO.setOrgName(sysUser.getOrgName());
        return ResponseParam.success().data(sysUserDTO);

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String userHash = "IrsdsONT2%2bsfQ59%2fa0JsvTHfKL8%3d";
        userHash = URLDecoder.decode(userHash,"UTF-8");
        System.out.println("userHash=" + userHash);
    }
}
