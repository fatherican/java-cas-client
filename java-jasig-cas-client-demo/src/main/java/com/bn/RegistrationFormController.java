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
@RequestMapping("/register.sso")
public class RegistrationFormController {

        private Log log = LogFactory.getLog(RegistrationFormController.class);
        @Autowired
        private Producer captchaProducer;


        @RequestMapping(method = RequestMethod.POST)
        public String processForm(
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
            String captchaId = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            String captcha =  request.getParameter("captcha");

            UserInfo userInfo = UserInfoHolder.getUserInfo();
            System.out.println("userInfo:"  + userInfo);

            if (log.isDebugEnabled()) {
                log.debug("Validating captcha response: '" + response + "'");
            }

            if (!StringUtils.equalsIgnoreCase(captchaId, captcha)) {
                System.out.println("================captcha error=================");
            }else{
                System.out.println("================captcha right=================");
            }

                return "registrationform";
        }

}