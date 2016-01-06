<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
request.setCharacterEncoding("UTF-8"); 
String pname = request.getParameter("playername");
String openid = request.getParameter("openid");
String sex = request.getParameter("sex");

String playerBlob = DataManager.getInstance().login(pname,openid,sex);
response.getWriter().print(playerBlob);
%>