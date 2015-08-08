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
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.cookie.CookieRetrievingCookieGenerator;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

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

    /**
     * 存放CAS服务器端用户属性  和 客户端CAS 服务器端用户属性的对应关系,可以添加更多的属性.
     */
    private UserPair[] userPairAttributes = {new UserPair("userName","username"),new UserPair("userId","userId")};


    private CookieRetrievingCookieGenerator cookieGenerator;

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
    protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion) {
        super.onSuccessfulValidation(request, response, assertion);
        Map<String, Object> userInfoMap = assertion.getPrincipal().getAttributes();
        StringBuffer userInfoJson = new StringBuffer("{");
        for(UserPair up : userPairAttributes){
            userInfoJson.append("\"" + up.getClientUserKey() + "\"" + ":");
            userInfoJson.append("\"" + (userInfoMap.get(up.getCasUserKey()) != null ? userInfoMap.get(up.getCasUserKey()).toString() : "") + "\",");
        }
        userInfoJson.append("}");
        //将userInfo json字符串转化成Userinfo对象
        final UserInfo ui = JSONObject.parseObject(userInfoJson.toString(),UserInfo.class);
        //设置用户 授权 登录时间，并且保存到requet域中，当UserInfoHoldFilter接收到该值后就会重新初始化
        ui.setValidateDate(new Date());
        request.setAttribute(AbstractCasFilter.CONST_CAS_USERINFO, ui);
        cookieGenerator.addCookie(request, response, JSONObject.toJSONString(ui));
    }

    public CookieGenerator getCookieGenerator() {
        return cookieGenerator;
    }

    public void setCookieGenerator(CookieRetrievingCookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }
}
