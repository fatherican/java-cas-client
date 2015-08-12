/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.validation;

import com.alibaba.fastjson.JSONObject;
import com.bn.framework.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.cookie.CookieRetrievingCookieGenerator;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Creates either a Cas30ProxyTicketValidator or a Cas30ServiceTicketValidator depending on whether any of the
 * proxy parameters are set.
 * <p/>
 * This filter can also pass additional parameters to the ticket validator.  Any init parameter not included in the
 * reserved list {@link org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter#RESERVED_INIT_PARAMS}.
 *
 * @author Jerome Leleu
 * @since 3.4.0
 */
public class Cas30ProxyReceivingTicketValidationFilter extends Cas20ProxyReceivingTicketValidationFilter {

    private boolean needRedirect = false;
    /**
     * 存放CAS服务器端用户属性  和 客户端CAS 服务器端用户属性的对应关系,可以添加更多的属性.
     */
    private UserPair[] userPairAttributes = {new UserPair("userName","username"),new UserPair("userId","userId")};
    private Map<String,String> casServerUserFieldPairToClientUserField;
    private Class<? extends UserInfo> userClass;
    /**
     * 用户cookie的生成
     */
    private CookieRetrievingCookieGenerator userInfoCookieGenerator;

    /**
     * 用户是否已经登录的标识的生成.
     */
    private CookieRetrievingCookieGenerator userLoginedFlagCookieGenerator;

    public Cas30ProxyReceivingTicketValidationFilter() {
        super(Protocol.CAS3);
        this.defaultServiceTicketValidatorClass = Cas30ServiceTicketValidator.class;
        this.defaultProxyTicketValidatorClass = Cas30ProxyTicketValidator.class;
    }


    /**
     * 当验证成功后，将用户信息转成JSON然后加密传递给前台.
     * @param request the HttpServletRequest.
     * @param response the HttpServletResponse.
     * @param assertion the successful Assertion from the server.
     */
    @Override
    protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion) throws IOException {
        super.onSuccessfulValidation(request, response, assertion);
        Map<String, Object> userInfoMap = assertion.getPrincipal().getAttributes();
        StringBuffer userInfoJson = new StringBuffer("{");
        Set<Map.Entry<String, String>> pairedSet = casServerUserFieldPairToClientUserField.entrySet();
        for (Map.Entry<String, String> stringStringEntry : pairedSet) {
            userInfoJson.append("\"" + stringStringEntry.getKey() + "\"" + ":");
            userInfoJson.append("\"" + (userInfoMap.get(stringStringEntry.getValue()) != null ? userInfoMap.get(stringStringEntry.getValue()).toString() : "") + "\",");
        }
        userInfoJson.append("}");
        //将userInfo json字符串转化成Userinfo对象
        final UserInfo ui = JSONObject.parseObject(userInfoJson.toString(),userClass);
        //设置用户 授权 登录时间，并且保存到requet域中，当UserInfoHoldFilter接收到该值后就会重新初始化
        ui.setValidateDate(new Date());
        request.setAttribute(AbstractCasFilter.CONST_CAS_USERINFO, ui);
        //用户信息加密后保存到cookie
        userInfoCookieGenerator.addCookie(request, response, JSONObject.toJSONString(ui));
        //用户登录标识加密后保存到cookie （通过JWT 加密，加密的值= 用户ID + 用户验证时间)
        userLoginedFlagCookieGenerator.addCookie(request,response, MD5.encode(ui.getUserId()+ui.getValidateDate().getTime()));
        //登录成功后跳转的地址
        String url = request.getParameter("redirectToUrl");
        if(StringUtils.isNotBlank(url)){
            response.sendRedirect(url);
            setNeedRedirect(true);
            return;
        }
    }

    public CookieGenerator getUserInfoCookieGenerator() {
        return userInfoCookieGenerator;
    }

    public void setUserInfoCookieGenerator(CookieRetrievingCookieGenerator userInfoCookieGenerator) {
        this.userInfoCookieGenerator = userInfoCookieGenerator;
    }

    @Override
    protected boolean needRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return this.needRedirect;
    }

    public boolean isNeedRedirect() {
        return needRedirect;
    }

    public void setNeedRedirect(boolean needRedirect) {
        this.needRedirect = needRedirect;
    }

    public UserPair[] getUserPairAttributes() {
        return userPairAttributes;
    }

    public void setUserPairAttributes(UserPair[] userPairAttributes) {
        this.userPairAttributes = userPairAttributes;
    }

    public Class getUserClass() {
        return userClass;
    }

    public void setUserClass(Class userClass) {
        this.userClass = userClass;
    }

    public Map<String, String> getCasServerUserFieldPairToClientUserField() {
        return casServerUserFieldPairToClientUserField;
    }

    public void setCasServerUserFieldPairToClientUserField(Map<String, String> casServerUserFieldPairToClientUserField) {
        this.casServerUserFieldPairToClientUserField = casServerUserFieldPairToClientUserField;
    }

    public CookieRetrievingCookieGenerator getUserLoginedFlagCookieGenerator() {
        return userLoginedFlagCookieGenerator;
    }

    public void setUserLoginedFlagCookieGenerator(CookieRetrievingCookieGenerator userLoginedFlagCookieGenerator) {
        this.userLoginedFlagCookieGenerator = userLoginedFlagCookieGenerator;
    }
}
