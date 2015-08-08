<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="org.jasig.cas.client.authentication.AttributePrincipalImpl"%>
<%@ page import="java.util.Enumeration" %>
<%@ page import="org.jasig.cas.client.validation.UserInfo" %>
<%@ page import="org.jasig.cas.client.util.UserInfoHolder" %>
<!DOCTYPE html>
<html>
<head>
	<title>java-jasig-cas-client-demo</title>
	<meta charset="UTF-8" />
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
</head>
<body>
<span>========aaaaa=========<%=request.getContextPath()%>=========aaaaaa======</span>
	<div class="container">
		<ul class="nav nav-tabs">
			<li><a href="<%=request.getContextPath()%>/index.jsp">Call the /index.jsp page</a></li>
			<li class="active"><a href="<%=request.getContextPath()%>/protected/index.jsp">You are on the /protected/index.jsp page</a></li>
			<!-- #### change with your own CAS server and your host name #### -->
			<li><a href="https://cas.com:8443/cas/logout?service=http://<%=request.getContextPath()%>">Call the CAS logout</a></li>
			<li><a href="<%=request.getContextPath()%>/protected/login.sso?redirectToUrl=http://www.baidu.com">protected Login</a></li>
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
			<% UserInfo user = UserInfoHolder.getUserInfo();
				if (user != null) { %>
			<p>remoteUser: <%=request.getRemoteUser()%></p>
			<p>User: <%=user.getUsername()%></p>
			<p>Attributes: <%=user.getUsername()%></p>
			<p>ID: <%=user.getUserId()%></p>
			<% } else { %>
			<p>Unauthenticated / anonymous user</p>
			<% } %>
		</h3>
	</div>
</body>
</html>
