<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
request.setCharacterEncoding("UTF-8"); 
String stropenid = request.getParameter("openid");
String playername = request.getParameter("playername");
String strsex = request.getParameter("sex");
String settingStr = request.getParameter("setting");

int sex = Integer.valueOf(strsex);

String playerBlob = DataManager.getInstance().login(stropenid,playername,sex,settingStr,request);
response.getWriter().print(playerBlob);
//response.getWriter().print("helloworld");
%>