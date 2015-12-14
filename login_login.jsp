<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%@ page import="net.sf.json.JSONObject"%>
<%
String pname = request.getParameter("playername");
String openid = request.getParameter("openid");
String sex = request.getParameter("sex");

//LoginAction  action = new LoginAction();
//String pdata = action.loginSimple(playerid);
String playerBlob = DataManager.getInstance().login(pname,openid,sex);
response.getWriter().print(playerBlob);
//System.out.println("aaa"+pwd);
%>