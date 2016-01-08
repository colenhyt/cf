<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.util.*"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pid = request.getParameter("playerid");
int playerid = Integer.valueOf(pid);
String data = DataManager.getInstance().get_info(playerid);
response.getWriter().print(data);
%>