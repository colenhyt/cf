<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%
String pid = request.getParameter("playerid");
String type = request.getParameter("type");
int playerid = Integer.valueOf(pid);
int typeid = Integer.valueOf(type);
String data = DataManager.getInstance().getData(playerid,typeid);
response.getWriter().print(data);
%>