<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
request.setCharacterEncoding("UTF-8"); 
String jsonstr = request.getParameter("jsonstr");

String playerBlob = DataManager.getInstance().login(jsonstr,request);
response.getWriter().print(playerBlob);
%>