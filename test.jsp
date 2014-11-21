<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>FreeCMS-gametest</title>
<script src="js/jquery-1.5.1.min.js"></script>
<script src="js/cookie.js"></script>
<script language="javascript">


  var htmlobj=$.ajax({url:"/cfserver/toplist_daylist.do?toplist.id=1",async:false});



var obj = eval ("(" + htmlobj.responseText + ")");


//setInterval( "foo()", 5000 ); 

</script>
<link rel="stylesheet" href="img/style.css" type="text/css" />
<link rel="stylesheet" href="img/style2.css" type="text/css" />
</head>
<body >
<form id="fm1" class="fm-v clearfix" action="toplist_daylist.do" method="post">
<div id="Logo">
	<div id="myDiv"></div>
    <div style="clear:both"></div>
    <div class="input_pwd">
						<input id="id" name="toplist.id" class="colorblur" tabindex="1" accesskey="n" type="text" value="" size="25" autocomplete="false"/>
	</div>
			<div class="input_post">
                        <input class="button"   name="submit" accesskey="l" value="测试action" tabindex="4" type="submit" />
                   
			</div>    
</div>
</form>
