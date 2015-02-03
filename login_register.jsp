<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%
String playername = request.getParameter("playername");
String straccount = request.getParameter("accountid");
String strsex = request.getParameter("sex");
int accountid = Integer.valueOf(straccount).intValue();
int sex = Integer.valueOf(strsex).intValue();

String addplayer = DataManager.getInstance().register(playername,accountid,(byte)sex);
response.getWriter().print(addplayer);
//System.out.println("add"+addplayer);
%>