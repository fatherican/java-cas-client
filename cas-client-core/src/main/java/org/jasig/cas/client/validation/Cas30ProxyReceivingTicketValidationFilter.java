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

import org.jasig.cas.client.Protocol;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

//    private C

    public Cas30ProxyReceivingTicketValidationFilter() {
        super(Protocol.CAS3);
        this.defaultServiceTicketValidatorClass = Cas30ServiceTicketValidator.class;
        this.defaultProxyTicketValidatorClass = Cas30ProxyTicketValidator.class;
    }

    @Override
    public void init() {
        super.init();

    }

    /**
     * 初始化cookieGenerator和Json WEB TOKEN相关信息.
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);

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

    }
}
