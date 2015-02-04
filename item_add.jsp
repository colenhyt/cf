<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.action.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="net.sf.json.JSONObject"%>
<%
String strparam = request.getParameter("item");
String strtype = request.getParameter("type");
int type = Integer.valueOf(strtype).intValue();

JSONObject ppObj = JSONObject.fromObject(strparam);

String retstr = "";
if (type==0){
 Saving item = (Saving)JSONObject.toBean(ppObj,Saving.class);
 retstr = SavingDataManager.getInstance().addSaving(item);
}

response.getWriter().print(retstr);
System.out.println("aaa"+retstr+";"+strtype);
%>