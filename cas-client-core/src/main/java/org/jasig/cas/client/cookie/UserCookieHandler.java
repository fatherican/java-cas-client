package org.jasig.cas.client.cookie;

import com.alibaba.fastjson.JSONObject;
import com.bn.framework.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kai on 2015/8/13.
 */
public class UserCookieHandler {
    /**
     * 用户是否已经登录的标识的生成.
     */
    private CookieRetrievingCookieGenerator userLoginedFlagCookieGenerator;
    /**
     * 用户详细信息cookie生成器.
     */
    private CookieRetrievingCookieGenerator  userInfoCookieGenerator;

    /**
     * 判断用户是否已经登录.
     * @param request
     * @param response
     * @return
     */
    public boolean isLogined(final HttpServletRequest request, final HttpServletResponse response){
        //用户信息
        String userInfoJsonStr = userInfoCookieGenerator.retrieveCookieValue(request);
        //用户登录标识的String字符串
        String cookieLoginFlag = userLoginedFlagCookieGenerator.retrieveCookieValue(request);
        if (StringUtils.isBlank(userInfoJsonStr) || StringUtils.isBlank(cookieLoginFlag)){
            return false;
        }
        UserInfo ui = JSONObject.parseObject(userInfoJsonStr, UserInfo.class);

        String orginLoginFlag = MD5.encode(getActualLogingFlag(ui, request));
        if (StringUtils.equals(orginLoginFlag, cookieLoginFlag)){
            return true;
        }
        return false;
    }


    /**
     * 获取原始登录信息标记.
     * 通过 userId + 验证日期 + 客户端IP 来标识一个用户.
     * @return
     */
    private String getActualLogingFlag(final UserInfo ui, final HttpServletRequest request){
        JSONObject jo = new JSONObject();
        jo.put("userId",ui.getUserId());
        jo.put("validateDate",ui.getValidateDate().getTime());
        jo.put("address",request.getRemoteAddr());
        return jo.toJSONString();
    }
    /**
     * 清空用户登录信息.
     *
     * @param request HTTP request containing a CAS logout message.
     */
    public void destoryUserCookie(final HttpServletRequest request, final HttpServletResponse response) {
        userInfoCookieGenerator.removeCookie(response);
        userLoginedFlagCookieGenerator.removeCookie(response);
    }


    /**
     * 增加用户登录的Cookie.
     * @param request
     * @param response
     * @param ui
     */
    public void addUserLoginCookie(final HttpServletRequest request, final HttpServletResponse response, UserInfo ui){
        //用户信息加密后保存到cookie
        userInfoCookieGenerator.addCookie(request, response, JSONObject.toJSONString(ui));
        //用户登录标识加密后保存到cookie
        userLoginedFlagCookieGenerator.addCookie(request,response, MD5.encode(getActualLogingFlag(ui, request)));
    }


    /**
     * 获取用户info.
     * @return
     */
    public String retrieveUserCookieValue(final HttpServletRequest request){
        return userInfoCookieGenerator.retrieveCookieValue(request);
    }



    public CookieRetrievingCookieGenerator getUserLoginedFlagCookieGenerator() {
        return userLoginedFlagCookieGenerator;
    }

    public void setUserLoginedFlagCookieGenerator(CookieRetrievingCookieGenerator userLoginedFlagCookieGenerator) {
        this.userLoginedFlagCookieGenerator = userLoginedFlagCookieGenerator;
    }

    public CookieRetrievingCookieGenerator getUserInfoCookieGenerator() {
        return userInfoCookieGenerator;
    }

    public void setUserInfoCookieGenerator(CookieRetrievingCookieGenerator userInfoCookieGenerator) {
        this.userInfoCookieGenerator = userInfoCookieGenerator;
    }

}
