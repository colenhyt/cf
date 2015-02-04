<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.*"%> 
<%@ page import="net.sf.json.JSONObject"%>
<%
String strparam = request.getParameter("stock");

JSONObject ppObj = JSONObject.fromObject(strparam);
Stock stock = (Stock)JSONObject.toBean(ppObj,Stock.class);

String retstr = StockDataManager.getInstance().addStock(stock);
response.getWriter().print(retstr);
System.out.println("aaa"+retstr);
%>