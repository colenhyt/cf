<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%@ page import="net.sf.json.JSONObject"%>
<%
String pp = request.getParameter("player");
JSONObject ppObj = JSONObject.fromObject(pp);
PlayerWithBLOBs playerBlob = (PlayerWithBLOBs)JSONObject.toBean(ppObj,PlayerWithBLOBs.class);
DataManager.getInstance().updatePlayer(playerBlob);
System.out.println("update:"+playerBlob.getPlayername());
%>