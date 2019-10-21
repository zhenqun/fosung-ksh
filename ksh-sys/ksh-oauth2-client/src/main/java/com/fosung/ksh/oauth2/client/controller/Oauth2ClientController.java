package com.fosung.ksh.oauth2.client.controller;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import com.fosung.ksh.oauth2.client.dto.TokenDTO;
import com.fosung.ksh.oauth2.client.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author wangyihua
 * @date 2019-06-03 11:48
 */
@RestController
@RequestMapping
public class Oauth2ClientController extends AppIBaseController {


    @Autowired
    private Oauth2ClientService clientService;


    /**
     * 获取code认证地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "login")
    public ResponseEntity<TokenDTO> getSSOLoginUrl(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam UserSource userSource) {
        return Result.success(clientService.login(username, password, userSource));
    }


    /**
     * 获取code认证地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "sso-login-url")
    public ResponseEntity<String> getSSOLoginUrl(@RequestParam String callback) throws UnsupportedEncodingException {
        return Result.success(clientService.getSSOLoginUrl(callback));
    }


    /**
     * 获取退出登录地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "sso-logout-url")
    public ResponseEntity<String> getSSOLogoutUrl(@RequestParam String callback) throws UnsupportedEncodingException {
        return Result.success(clientService.getSSOLogoutUrl(callback));
    }


    /**
     * 获取sso平台访问token
     *
     * @param code     用户从sso平台登录成功后带回来的code
     * @param callback sso回调地址，需要与获取code时回调地址一致
     * @return token信息
     */
    @RequestMapping("user-by-code")
    public ResponseEntity<SysUser> getSsoAccessToken(String code,
                                                     String callback) {
        TokenDTO token = clientService.getSsoAccessToken(code, callback);
        SysUser sysUser = clientService.getUserByToken(token);
        return Result.success(sysUser);
    }

    /**
     * 通过token获取用户详细信息
     *
     * @return
     */
    @RequestMapping("user/info")
    public ResponseEntity<SysUser> getUserByToken(HttpServletRequest request) {
        String token = request.getHeader("x-token");
        if (UtilString.isEmpty(token)) {
            throw new AppException("", HttpStatus.UNAUTHORIZED.value() + "", "用户登录失效，请重新登录");
        } else {
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setAccess_token(token);
            SysUser sysUser = clientService.getUserByToken(tokenDTO);
            if (sysUser == null) {
                throw new AppException("", HttpStatus.UNAUTHORIZED.value() + "", "用户登录失效，请重新登录");
            }
            return Result.success(sysUser);
        }
    }
}
