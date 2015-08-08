package com.bn;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.UserInfoHolder;
import org.jasig.cas.client.validation.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/protected/login.sso")
public class ProtectedLoginController {


        @RequestMapping
        public String processForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String redirectToUrl = request.getParameter("redirectToUrl");
            if(StringUtils.isNotBlank(redirectToUrl)){
                response.sendRedirect(redirectToUrl);
            }
           return null;
        }

}