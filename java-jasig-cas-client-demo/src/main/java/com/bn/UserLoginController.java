package com.bn;

import org.jasig.cas.client.cookie.UserCookieHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Created by kai on 2015/8/14.
 */
@Controller
public class UserLoginController {
    @NotNull
    @Resource(name = "userLoginHandler")
    private UserCookieHandler userCookieHandler;


    /**
     * 判断用户是否已经登录.
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "isLogin.sso", method = RequestMethod.GET)
    @ResponseBody
    public String isLogin(final HttpServletRequest request, final HttpServletResponse response){
        boolean result = userCookieHandler.isLogined(request, response);
        return result ? "true" : "false";
    }







}
