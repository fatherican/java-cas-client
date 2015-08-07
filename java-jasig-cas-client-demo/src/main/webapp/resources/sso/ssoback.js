var SSO = {
    createSSO: function(options){
        var sso = {};
        var $form;//动态创建的form表单
        var validateLoginServiceUrl;//该地址用来验证登录的结果和初始化客户端Session
        var casServerLoginUrl;//该地址用来向cas服务器请求LoginTicket和提交form表单

        /**
         * 初始化cas调用的整个过程中需要用到的URL
         */
        function initUrl(){
            /**
             * 业务系统验证登录结果的的回调URL
             */
            var bussinessServerUrl = $("#bussinessServerUrl").val();
            validateLoginServiceUrl = "validateLogin";//该地址用来验证登录的结果
            if (!bussinessServerUrl.endWith("/")) {
                validateLoginServiceUrl = bussinessServerUrl+ "/" + validateLoginServiceUrl;
            }else{
                validateLoginServiceUrl = bussinessServerUrl + validateLoginServiceUrl;
            }

            /**
             * 构造casServer获取LoginTicket的url
             */
            var casServerUrl =  $("#casServerUrl").val();
            casServerLoginUrl =  "login";
            if (!casServerUrl.endWith("/")) {
                casServerLoginUrl = casServerUrl+ "/" + casServerLoginUrl;
            }else{
                casServerLoginUrl = casServerUrl + casServerLoginUrl;
            }
        }

        /**
         * 初始化form表单
         */
        function initSubmitFormStruct(){
            $form = $(
                "<form action='"+casServerLoginUrl+"' method='post' id='sso_objForm_dynamic' name='objForm' target='sso_iframe_dynamic'>"+
                "<input name='username'  id='username' value=''/>"+
                "<input name='password'  id='password' value=''/>"+
                "<input name='isAjax'  id='isAjax' value='true'/>"+
                "<input name='isIframe'  id='isIframe' value='true'/>"+
                "<input type='text' id='lt' name='lt' value='' />"+
                "<input type='text' id='flowExecutionkey'  name='flowExecutionkey' value='' />"+
                "<input type='text' id='service'  name='service' value='"+validateLoginServiceUrl+"' />"+
                "<input type='text' id='callback'  name='callback' value='sso_callback' />"+
                "<input type='text' id='captcha'  name='captcha' value='' />"+
                "</form>"
            );
            $("#sso_iframe_dynamic").remove();//如果存在就先清除掉
            var iframeHtml = "<iframe style='display: none' id='sso_iframe_dynamic' name='sso_iframe_dynamic'>";
            $("body").append(iframeHtml);
        }

        /**
         * 获取LoginTicket
         */
        function getLT(){
            var flowExecutionkey = $form.find("#flowExecutionkey").val();
            $.getJSON(casServerLoginUrl+"?getLt=true&service="+validateLoginServiceUrl+"&isAjax=true&flowExecutionkey="+ flowExecutionkey +"&callback=?",function(response){
                if(response != null){
                    //用户去获取LoginTicket的时候发现，用户已经通过SSO登陆过、
                    // 所以不需要再登录，带着ST去初始化客户端session
                    if (response["origin"] == "successView") {
                        initClientSession(response["serviceTicketId"]);
                    }else if (response["origin"] == "loginView") {//获取loginTicket返回结果
                        if(response["state"] == "success"){//获取loginTicket成功
                            initFormKey(response);
                        }else{//获取loginTicket失败,有可能是当前的服务在单点登录平台不支持
                            getLoginTicketFailCallBack(response["message"]);
                        }
                    }
                }else{
                    alert("非法获取LoginTicket");
                }
            });
        }

        /**
         *初始化form表单的loginTicket和flowExecutionkey
         */
        function initFormKey(response){
            var loginTicket = response["lt"];
            var flowExecutionkey = response["execution"];
            var needCaptcha = response["needCaptcha"];//是否需要验证码的标识
            $form.find("#lt").val(loginTicket);
            $form.find("#flowExecutionkey").val(flowExecutionkey);
            if(needCaptcha){
                sso.showCaptcha();
                getCaptcha();
            }else{
                sso.hideCaptcha();
            }
        }

        /**
         *获取loginTicket失败后的回调.
         * @param messages 获取LoginTicket失败的原因
         */
        function getLoginTicketFailCallBack(messages){
            var size = messages.length;
            var messageStr = "";
            for(var i = 0; i < size; i++){
                messageStr = messageStr + messages.get(i)["defaultMessage"];
            }
            alert("获取票据失败,原因："+ messageStr +",请重试");
        }

        /**
         *初始化客户端Session
         *@param serviceTicketId  st.
         */
        function initClientSession(serviceTicketId){
            $.ajax({
                url:validateLoginServiceUrl+"?ticket="+serviceTicketId,
                type:'get',
                success:function(data){
                    sso.successCallback();
                },
                error: function(xhr, status, error){
                    sso.failCallback("登录失败");
                }
            });
        }

        /**
         * 获取验证码
         * @param flowExecutionkey 流标识.
         */
        function getCaptcha(){
            var flowExecutionkey = $form.find("#flowExecutionkey").val();
            var src='./captcha-image.sso?flowExecutionkey='+flowExecutionkey+'&d='+ (new Date().getTime());
            $("#"+sso.captchaImgId).attr("src",src);
        }

        /**
         * 判断submit提交的结果
         */
        function callback(state,message,serviceTicket){
            if("success" == state){//用户登录成功并且session也已经初始化成功
                $("#sso_iframe_dynamic").remove();//清除掉iframe
                sso_success_callback();
            }else{//用户登录失败
                getLT();
                sso_fail_callback(message);
            }
        }

        /**
         *提交表单
         */
        function submit(username,password,captcha){
            $form.find("#username").val(username);
            $form.find("#password").val(password);
            $form.find("#captcha").val(captcha);
            $form.submit();
        }



        /**
         * 打印动态生成的Form表单，该表单中字段的值不会因为动态赋值后显示在控制台，请调用getValue来查看具体的值.
         */
        function getFormHtml(){
            console.log($form.html());
        }
        /**
         * 获取form表单中某个字段的值
         */
        function getValue(id){
            console.log(id+":"+$form.find("#"+id).val());
        }

        sso.catpchaInputId =options.catpchaInputId;//验证码文本框
        sso.captchaImgId =options.captchaImgId;//验证码图片ID
        sso.successCallback =options.successCallback;
        sso.failCallback =options.failCallback;
        sso.showCaptcha =options.showCaptcha;
        sso.hideCaptcha =options.hideCaptcha;
        sso.getCaptcha =getCaptcha;
        sso.submit = submit;
        sso.getFormHtml=getFormHtml;
        sso.getValue=getValue;
        /**将submit提交后的结果判断，挂到window域下面，这样通过parent.window.sso_callback调用的时候就可以调用的到*/
        window.sso_callback=callback;
        window.sso_success_callback=options.successCallback;
        window.sso_fail_callback=options.failCallback;

        sso.init = function(){//当对象初始化成功后即调用获取loginTicket的方法
            addSupportEndWith();//增加String对象的EndWith支持
            addSupportStartWith();//增加String对象的StartWith支持
            //初始化调用地址
            initUrl();
            //初始化form表单
            initSubmitFormStruct();
            //获取登录票据
            getLT();
        };
        sso.init();

        /** 扩展String的endWith方法*/
        function addSupportEndWith(){
            String.prototype.endWith=function(s){
                if(s==null||s==""||this.length==0||s.length>this.length)
                    return false;
                if(this.substring(this.length-s.length)==s)
                    return true;
                else
                    return false;
                return true;
            }
        }

        /** 扩展String的startWith方法*/
        function addSupportStartWith() {
            String.prototype.startWith = function (s) {
                if (s == null || s == "" || this.length == 0 || s.length > this.length)
                    return false;
                if (this.substr(0, s.length) == s)
                    return true;
                else
                    return false;
                return true;
            }
        }

        return sso;
    }


};