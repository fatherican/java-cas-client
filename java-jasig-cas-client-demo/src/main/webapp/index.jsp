<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.jasig.cas.client.authentication.AttributePrincipalImpl"%>
<%@ page import="java.util.Enumeration" %>
<jsp:include page="WEB-INF/sso.jsp"/>
<!DOCTYPE html>
<br>
<head>
	<title>java-jasig-cas-client-demo</title>
	<meta charset="UTF-8" />
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
	<script type="text/javascript" src="//lib.sinaapp.com/js/jquery/1.10.2/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="resources/sso/sso.js"></script>

	<script type="text/javascript">


		var sso;
		function testCode(){
			sso = SSO.createSSO({
										"successCallback":function(){
											alert("登录成功");
										},
										"failCallback":function(message){
											alert(message);
										},
										"showCaptcha":function(){
											$("#captchaDiv").show();
										},
										"hideCaptcha":function(){
											$("#captchaDiv").hide();
										},
										"catpchaInputId":"catpchaInputId",
										"captchaImgId":"captchaImgId"
								});

//			sso.submit("1","2","3");




		}


		function ssoSubmit(){
			sso.ssoSubmit($("#username").val(),$("#password").val(),$("#captcha").val());
		}

		function getFormIner(){
			sso.getFormHtml();
		}


		function testSubmit(){
			sso.ssoSubmit();
		}


		function getFormValue(){
			var attributeId = $("#formAttributeId").val();
			sso.getValue(attributeId);
		}

	</script>





	<script type="text/javascript">








		function submitForm(){
			document.objForm.submit();//如果成功，iframe在进行replace的时候回自动验证ST初始化session的信息.
		 }






	</script>

</head>
<br>



<input type="button" value="initSSO" onclick="testCode()"/>
<input type="button" value="getFormHtml" onclick="getFormIner()"/>
</br>
<input type="text" id="formAttributeId" >
<input type="button" value="getFormValue" onclick="getFormValue()"/>
</br>
username:<input id="username" type="text"/>
password:<input id="password" type="text"/>
<div id="captchaDiv">

	captcha:<input id="captcha" type="text"/>

	<img src="" id="captchaImgId" onclick="sso.getCaptcha()" title="看不清，换一张">
</div>
<input type="button"  onclick="ssoSubmit()" value="ssoSubmit" />
<form action="./register.sso" method="post">
	<%--<img src="./captcha-image.sso" onclick="this.src='./captcha-image.sso?d='+ (new Date().getTime())" width="350" height="30">--%>
	<input type='text' name='captcha' value=''>
	<input type="submit" value="提交">
</form>



<span>========aaaaa=========<%=request.getContextPath()%>=========aaaaaa======</span>
	<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a href="./index.jsp">You are on the /index.jsp page</a></li>
			<li><a href="<%=request.getContextPath()%>/protected/index.jsp">Call the /protected/index.jsp page</a></li>
			<!-- #### change with your own CAS server and your host name #### -->
			<li><a href="http://localhost:8888/cas/logout?service=http://localhost:8080">Call the CAS logout</a></li>
		</ul>
		<h3>
			<%
					HttpSession session2 =    request.getSession();
				Enumeration<String> enu = session2.getAttributeNames();
				%>
			<%
				while(enu.hasMoreElements()){
						%>
					<p>attribute:  <%=enu.nextElement()%></p>
			<%
				}

			%>
			<% AttributePrincipalImpl user = (AttributePrincipalImpl) request.getUserPrincipal();
			if (user != null) { %>
				<p>remoteUser: <%=request.getRemoteUser()%></p>
				<p>User: <%=user.getName()%></p>
				<p>Attributes: <%=user.getAttributes()%></p>
			<% } else { %>
				<p>Unauthenticated / anonymous user</p>
			<% } %>
		</h3>
	</div>

	<div>
		<input type="button" class="bt" value="getLT" onclick="getLT();">

		<%--<form action="https://cas.com:8443/cas/login" method="post" id="objForm" name="objForm" target="ssoIframe">--%>
			<%--userName:<input name="username"  id="username" value="admin"/>--%>
			<%--password:<input name="password"  id="password" value="admin"/>--%>
			<%--isAjax:<input name="isAjax"  id="isAjax" value="true"/>--%>
			<%--isIframe:<input name="isIframe"  id="isIframe" value="true"/>--%>
			<%--loginTicket:<input type="text" id="lt" name="lt" value="" />--%>
			<%--flowExecutionkey:<input type="text" id="flowExecutionkey"  name="flowExecutionkey" value="" />--%>
			<%--service:<input type="text" id="service"  name="service" value="http://client1.com:8081/client/validateLogin" />--%>
			<%--callback:<input type="text" id="callback"  name="callback" value="callback" />--%>
			<%--captcha:<input type="text" id="captcha"  name="captcha" value="callback" />--%>
			<%--<img  id="captchaImage" onclick="getCaptcha()" width="350" height="30">--%>
			<%--</br>--%>
			<%--<input type="button" value="submit" onclick="submitForm();">--%>
		<%--</form>--%>
	</div>
<iframe name="ssoIframe" id="ssoIframe" height="300" width="100%" />


</body>
</html>
