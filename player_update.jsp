<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pid = request.getParameter("playerid");
String tid = request.getParameter("type");
int playerid = Integer.valueOf(pid);
int type = Integer.valueOf(tid);
DataManager.getInstance().update(playerid,type);
%>