<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pid = request.getParameter("playerid");
String tid = request.getParameter("type");
String pwd = request.getParameter("pwd");
String str1 = request.getParameter("str");
String str = DataManager.getInstance().addXXX(pwd,Integer.valueOf(tid),Integer.valueOf(pid),str1);
response.getWriter().print(str);
%>
<html>
pwd,str
</html>
