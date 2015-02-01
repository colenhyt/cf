<%@ page import="cn.hd.mgr.*"%> 
<%@ page import="cn.hd.cf.model.*"%>
<%@ page import="cn.hd.cf.service.*"%>
<%@ page import="cn.hd.util.*"%>
<%@ page import="redis.clients.jedis.Jedis"%> 
<%
String ppid = request.getParameter("playerid");
int playerid = Integer.parseInt(ppid);
		
RedisClient redisClient = new RedisClient();
Jedis jedis = redisClient.jedis;
String playerBlob = jedis.hget(PlayerService.ITEM_KEY,Integer.valueOf(playerid).toString());
jedis.close();
redisClient = null;
response.getWriter().print(playerBlob);
//System.out.println("1 "+playerBlob);
%>