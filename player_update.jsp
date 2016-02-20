<%@ page import="cn.hd.mgr.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pid = request.getParameter("playerid");
String tid = request.getParameter("type");
String itemid = request.getParameter("itemid");
String amountstr = request.getParameter("amount");
int playerid = Integer.valueOf(pid);
int type = Integer.valueOf(tid);
String retstr = DataManager.getInstance().update(playerid,type,itemid,amountstr);
response.getWriter().print(retstr);
%>