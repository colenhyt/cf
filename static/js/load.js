var arr =[
          "http://192.168.123.1:8080/cf/static/js/config.js",
        "http://192.168.123.1:8080/cf/static/js/util.js",
        "http://192.168.123.1:8080/cf/static/data/savingdata.js",
        "http://192.168.123.1:8080/cf/static/data/initdata.js",
        "http://192.168.123.1:8080/cf/static/data/quotedata.js",
        "http://192.168.123.1:8080/cf/static/data/signindata.js",
        "http://192.168.123.1:8080/cf/static/data/eventdata.js",
        "http://192.168.123.1:8080/cf/static/data/titledata.js",
        "http://192.168.123.1:8080/cf/static/data/insuredata.js",
        "http://192.168.123.1:8080/cf/static/data/stockdata.js",
        "http://192.168.123.1:8080/cf/static/data/questdata.js",
        "http://192.168.123.1:8080/cf/static/js/htmlutil.js",
        "http://192.168.123.1:8080/cf/static/js/common.js",
        "http://192.168.123.1:8080/cf/static/js/game.js",
         "http://192.168.123.1:8080/cf/static/js/toplist.js",
          "http://192.168.123.1:8080/cf/static/js/share.js",
        "http://192.168.123.1:8080/cf/static/js/playerinfo.js",
        "http://192.168.123.1:8080/cf/static/js/login.js",
        "http://192.168.123.1:8080/cf/static/js/player.js",
        "http://192.168.123.1:8080/cf/static/js/quest.js",
        "http://192.168.123.1:8080/cf/static/js/signin.js",
       "http://192.168.123.1:8080/cf/static/js/insure.js",
        "http://192.168.123.1:8080/cf/static/js/stockdetail.js",
       "http://192.168.123.1:8080/cf/static/js/stock.js",
       "http://192.168.123.1:8080/cf/static/js/bank.js",
       "http://192.168.123.1:8080/cf/static/js/saving.js",
       "http://192.168.123.1:8080/cf/static/js/help.js",
       "http://192.168.123.1:8080/cf/static/js/title.js",
        "http://192.168.123.1:8080/cf/static/js/message.js",
        "http://192.168.123.1:8080/cf/static/js/uplevel.js",
       "http://192.168.123.1:8080/cf/static/js/event.js",
       "http://192.168.123.1:8080/cf/static/js/main.js"
];

var marr = new Array(); // 标记arr中的是否加载完毕
var maxtime = 20000;  // 设置加载失败时间20秒
var timing = 0; // 记录当前的时间
var mtest = document.getElementById("test");  //  进度条容器
var mtestdiv = mtest.getElementsByTagName("div")[0];  // 进度条线
var mtestspan = mtest.getElementsByTagName("span")[0]; // 进度条数字
for(var i =0,j=arr.length;i<j;i++){  // 初始化数组，并将每一项的值都设为false
    marr[i] = false;
}
function include_js(file,index) {  // 检测是否加载完成,并添加到head中
    var _doc = document.getElementsByTagName('head')[0];
    var js = document.createElement('script');
    js.setAttribute('type', 'text/javascript');
    js.setAttribute('src', file);
    _doc.appendChild(js);
    if (document.all) { //如果是IE
        js.onreadystatechange = function () {
            if (js.readyState == 'loaded' || js.readyState == 'complete') {
                marr[index] = true;
            }
        }
    }
    else {
        js.onload = function () {
            marr[index] = true;
        }
    }
}

for(var i = 0,j=arr.length;i<j;i++){  // 向head中添加每一项
    include_js(arr[i],i);
}
 
var stop = setInterval(function(){
    var index = 0;  // 统计当前的总共加载完毕的个数
    for(var i=0,j=marr.length;i<j;i++){
        if(marr[i] === true){
            index++;
        }
    }
                
    mtestspan.innerHTML= parseInt((index/marr.length)*100)+"%";
    mtestdiv.style.width = parseInt((index/marr.length)*100)*2+"px";

    if(index === marr.length){  // 加载完成
        mtest.parentNode.removeChild(mtest);
        clearInterval(stop);
    }
    
    timing+=60;
    if(timing>maxtime){  //  加载失败
        clearInterval(stop);
        mtest.innerHTML= "页面加载失败！";
    }else
      mtest.innerHTML= "页面加载成功！";
},60);