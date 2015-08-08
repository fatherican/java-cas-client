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
package org.jasig.cas.client.util;

import com.alibaba.fastjson.JSONObject;
import org.jasig.cas.client.cookie.CookieRetrievingCookieGenerator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.UserInfo;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Places the assertion in a ThreadLocal such that other resources can access it that do not have access to the web tier session.
 *
 * @author yangkai
 * @version $Revision: 11728 $ $Date: 2015-08-08 14:20:43 $
 * @since 4.1.0
 */
public final class UserInfoThreadLocalFilter implements Filter {

    private CookieRetrievingCookieGenerator cookieGenerator;


    public void init(final FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(false);
//        response.addCookie(new Cookie());
        try {
//            if(session == null){//用户尚未登录，所以不解析从客户端发来的cookie用户数据，而且将用户的cookie数据置为空
//                cookieGenerator.removeCookie(response);
//            }else{
                final String userInfoJsonStr = cookieGenerator.retrieveCookieValue(request);
                //登录成功后，从request中获取用户信息，忽略客户端的cookie
                if ( request.getAttribute(AbstractCasFilter.CONST_CAS_USERINFO) != null){
                    UserInfoHolder.setUserInfo((UserInfo) request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
                }else{
                    if( userInfoJsonStr != null ){
                        UserInfoHolder.setUserInfo(JSONObject.parseObject(userInfoJsonStr, UserInfo.class));
                    }
                }
//            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AssertionHolder.clear();
        }
    }

    public void destroy() {
        // nothing to do
    }


    public CookieRetrievingCookieGenerator getCookieGenerator() {
        return cookieGenerator;
    }

    public void setCookieGenerator(CookieRetrievingCookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }
}
