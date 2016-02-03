<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pname = request.getParameter("name");
String pwd = request.getParameter("pwd");
String str = DataManager.getInstance().getXXX(pname,pwd);
response.getWriter().print(str);
%>