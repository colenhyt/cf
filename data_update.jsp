<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="com.alibaba.fastjson.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String tid = request.getParameter("type");
String playerstr = request.getParameter("playerid");
String itemstr = request.getParameter("itemid");
String qtystr = request.getParameter("qty");
String pricestr = request.getParameter("price");
String amountstr = request.getParameter("amount");
int playerid = Integer.valueOf(playerstr);
int itemid = Integer.valueOf(itemstr);
int qty = Integer.valueOf(qtystr);
float price = Float.valueOf(pricestr);
float amount = Float.valueOf(amountstr);
int type = Integer.valueOf(tid);
String retStr = "";
switch (type){
	case 1:
	retStr = SavingManager.getInstance().add(playerid,itemid,qty,price,amount);
	break;	
	case 2:
	retStr = InsureManager.getInstance().add(playerid,itemid,qty,price,amount);
	break;	
	case 3:
	retStr = StockManager.getInstance().add(playerid,itemid,qty,price,amount);
	break;		
}
response.getWriter().print(retStr);
%>