package com.bn;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.ssl.HttpsURLConnectionFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

@Controller
public class CaptchaImageCreateController {
        private Producer captchaProducer = null;
        
        @Autowired
        public void setCaptchaProducer(Producer captchaProducer) {
                this.captchaProducer = captchaProducer;
        }

        @RequestMapping("/captcha-image.sso")
        public ModelAndView handleRequest(
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
                // Set to expire far in the past.
                response.setDateHeader("Expires", 0);
                // Set standard HTTP/1.1 no-cache headers.
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
                response.addHeader("Cache-Control", "post-check=0, pre-check=0");
                // Set standard HTTP/1.0 no-cache header.
                response.setHeader("Pragma", "no-cache");
                String remoteUser = request.getRemoteUser();


                // return a jpeg
                response.setContentType("image/jpeg");

                // create the text for the image
//
                WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
                ServletContext servletContext = webApplicationContext.getServletContext();
                String casGetCaptchaUrl = servletContext.getInitParameter("casServerCaptchaUrl");
                String flowExecutionkey = request.getParameter("flowExecutionkey");
                String d = request.getParameter("d");
                //将流标识 带给 cas server
                casGetCaptchaUrl = new StringBuffer(casGetCaptchaUrl).append("?flowExecutionkey=").
                                                                      append(flowExecutionkey).
                                                                      append("&d=").append(d).toString() ;
                String capText = CommonUtils.getResponseFromServer(new URL(casGetCaptchaUrl), new HttpsURLConnectionFactory(), null);

                // create the image with the text
                BufferedImage bi = captchaProducer.createImage(capText);

                ServletOutputStream out = response.getOutputStream();
                
                // write the data out
                ImageIO.write(bi, "jpg", out);
                try
                {
                        out.flush();
                }
                finally
                {
                        out.close();
                }
                return null;
        }
}