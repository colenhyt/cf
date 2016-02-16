<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.util.*"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String countstr = request.getParameter("count");
int count = 1000;
if (countstr!=null)
	count = Integer.valueOf(countstr);
String data = ToplistManager.getInstance().getActivityList(count);
response.getWriter().print(data);
%>