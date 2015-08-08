package com.bn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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