<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pname = request.getParameter("name");
String pid = request.getParameter("playerid");
String pwd = request.getParameter("pwd");
String str = DataManager.getInstance().getXXX(pname,pid,pwd);
response.getWriter().print(str);
%>