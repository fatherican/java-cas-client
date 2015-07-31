<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.jasig.cas.client.authentication.AttributePrincipalImpl"%>
<%@ page import="java.util.Enumeration" %>
<!DOCTYPE html>
<html>
<head>
	<title>java-jasig-cas-client-demo</title>
	<meta charset="UTF-8" />
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
	<script type="text/javascript" src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
	<script type="text/javascript">
		function getLT(){
			var flowExecutionkey = $("#flowExecutionkey").val();
			$.getJSON("https://cas.com:8443/cas/login?getLt=true&service=http://client1.com:8081/client1/validateLogin&isAjax=true&flowExecutionkey="+ flowExecutionkey +"&callback=?",function(response){
					if(response != null){
						if (response["origin"] == "successView") {//登录成功界面的JSON，表示用户已经登陆过，所以不需要在登录
							$.ajax({
								url:"http://client1.com:8081/client1/validateLogin?ticket="+response["serviceTicketId"],
								type:'get',
								success:function(data){
									alert("自动登录成功");
								},
								error: function(xhr, status, error){
									alert("自动登录失败");
								}
							});
						}else if (response["origin"] == "loginView") {//登录界面的JSON
							if(response["state"] == "success"){//获取loginTicket成功
								$("#lt").val(response["lt"]);
								$("#flowExecutionkey").val(response["execution"]);
							}else{//获取loginTicket失败,有可能是当前的服务在单点登录平台不支持
								var size = response["message"].length;
								for(var i = 0; i < size; i++){
									console.log(response["message"].get(i)["defaultMessage"]);
								}
							}

						}
					}else{
						alert("非法获取LoginTicket");
					}

			});
		}


		function submitForm(){
			document.objForm.submit();//如果成功，iframe在进行replace的时候回自动验证ST初始化session的信息.

		}

		function callback(state,message,serviceTicket){
			if("success" == state){//用户登录成功并且session也已经初始化成功
				alert("登录成功");
			}else{//用户登录失败，重新获取loginTicket
				//TODO 重新获取loginTicket
				alert("用户登录失败，失败原因:"+message);
			}
		}




	</script>

</head>
<body>

<input type="button" value="ajaxServer" onclick="ajaxServer()"/>
	   <div id="serverContent">


	   </div>
<span>========aaaaa=========<%=request.getContextPath()%>=========aaaaaa======</span>
	<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a href="/index.jsp">You are on the /index.jsp page</a></li>
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

		<form action="https://cas.com:8443/cas/login" method="post" id="objForm" name="objForm" target="ssoIframe">
			userName:<input name="username"  id="username" value="admin"/>
			password:<input name="password"  id="password" value="admin"/>
			isAjax:<input name="isAjax"  id="isAjax" value="true"/>
			isIframe:<input name="isIframe"  id="isIframe" value="true"/>
			loginTicket:<input type="text" id="lt" name="lt" value="" />
			flowExecutionkey:<input type="text" id="flowExecutionkey"  name="flowExecutionkey" value="" />
			service:<input type="text" id="service"  name="service" value="http://client1.com:8081/client1/validateLogin" />
			callback:<input type="text" id="callback"  name="callback" value="callback" />
			</br>
			<input type="button" value="submit" onclick="submitForm();">
		</form>
	</div>
<iframe name="ssoIframe" id="ssoIframe" height="300" width="300" />
</body>
</html>
