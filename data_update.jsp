<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="com.alibaba.fastjson.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String json = request.getParameter("data");
String tid = request.getParameter("type");
int type = Integer.valueOf(tid);
System.out.println(json);
String retStr = "";
switch (type){
	case 1:
	Saving s =	JSON.parseObject(json, Saving.class);
	retStr = SavingManager.getInstance().add(s);
	break;	
	case 2:
	Insure s2 =	JSON.parseObject(json, Insure.class);
	retStr = InsureManager.getInstance().add(s2);
	break;	
	case 3:
	Stock s3 =	JSON.parseObject(json, Stock.class);
	retStr = StockManager.getInstance().add(s3);
	break;		
}
	System.out.println(retStr);
response.getWriter().print(retStr);
%>