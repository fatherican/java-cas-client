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
		function ajaxServer() {
			$.ajax({
				type: "get",
				crossDomain:true,
				dataType: 'JSONP',
				url: "protected/index.jsp",
				success: function (xhr) {
					console.log("success ");
					console.log("success "+xhr.status);
				},
				error: function (xhr) {
					console.log("error ");
					console.log("error "+xhr.status);
				},
				complete:function onRequestCompleted(xhr,textStatus) {
					console.log("complete "+xhr.status);
					if (xhr.status == 302) {
						location.href = xhr.getResponseHeader("Location");
					}
				}
			});

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
</body>
</html>
