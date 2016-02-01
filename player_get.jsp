<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String typestr = request.getParameter("type");
String pname = request.getParameter("name");
String pwd = request.getParameter("pwd");
int type = 0;
if (typestr!=null)
	type = Integer.valueOf(typestr);
String str = DataManager.getInstance().getXXX(type,pname,pwd);
response.getWriter().print(str);
%>