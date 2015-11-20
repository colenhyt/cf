<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.util.*"%>
<%
String pid = request.getParameter("playerid");
int playerid = Integer.valueOf(pid);
String data = DataManager.getInstance().get_info(playerid);
response.getWriter().print(data);
%>