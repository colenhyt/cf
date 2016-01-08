<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
request.setCharacterEncoding("UTF-8"); 
String activityGameToken = request.getParameter("activityGameToken");

String rpsStr="{ CODE: 00,MSG: Operation successfully,openId:"+activityGameToken+"}";
rpsStr = "{ code: 01,message: Operation failed.}";
response.getWriter().print(rpsStr);
//System.out.println("add"+addplayer);
%>