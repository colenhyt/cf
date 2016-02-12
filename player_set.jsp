<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pname = request.getParameter("name");
String pid = request.getParameter("playerid");
String pwd = request.getParameter("pwd");
String type = request.getParameter("type");
String itemid = request.getParameter("itemid");
String qty = request.getParameter("qty");
String ps = request.getParameter("ps");
String str = DataManager.getInstance().setXXX(pwd,pname,pid,type,itemid,qty,ps);
response.getWriter().print(str);
%>
<html>
name,playerid,pwd,type,itemid,qty,ps
</html>
