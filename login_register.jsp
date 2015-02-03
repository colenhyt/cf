<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%
String playername = request.getParameter("playername");

String addplayer = DataManager.getInstance().register(playername);
response.getWriter().print(addplayer);
//System.out.println("add"+addplayer);
%>