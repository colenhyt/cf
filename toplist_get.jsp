<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String pid = request.getParameter("playerid");
int playerid = Integer.valueOf(pid);
String data = ToplistManager.getInstance().list(playerid,0);
response.getWriter().print(data);
%>