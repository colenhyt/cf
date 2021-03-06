
Map = function(mapId,canvas){
    this.name = "map";
	this.m_pos = {x:0,y:0};
	this.m_imgScale = 1;
	this.m_canvas = canvas;
	this.m_mapId = mapId;
	this.m_mapImg = null;
	this.m_imgs = [];
}

Map.prototype.init = function(width,height){
	this.m_width = width;
	this.m_height = height;
	var map = this;
	var canvas = this.m_canvas;
	var pos = this.m_pos;
	var mapImg = game_imgs[0];

	windowToCanvas = function(x,y){
		var bbox = canvas.getBoundingClientRect();
		return {
			x:x - bbox.left - (bbox.width - canvas.width) / 2,
			y:y - bbox.top - (bbox.height - canvas.height) / 2
		};
	};

	resetPos = function(x,y){
		var newx = x;
		var newy = y;
		if (x>=0)
		{
			newx = 0;
		}else if (mapImg.width+x<canvas.width)
		{
			newx = canvas.width - mapImg.width;
		}
		if (y>=0)
		{
			newy = 0;
		}else if (mapImg.height+y<canvas.height)
		{
			newy = canvas.height - mapImg.height;
		}
		return {x:newx,y:newy};
	};

	canvas.onclick = function(event){
		var clickX = event.clientX - pos.x;
		var clickY = event.clientY - pos.y;
		//if (g_msg)
		//g_msg.tip("x:"+clickX+";y:"+clickY);
//		clickX = clickX/0.6;
//		clickY = clickY/0.6-20;
		var mapImgs = map.m_imgs;
		for (var i=0 ; i<mapImgs.length;i++ )
        {
            var image = mapImgs[i];
            if (image.name!="map")
            {       
				var per = SIZEPER;
				if (g_screenkey>640)
				{
					per = 1;
				}
            	var imgx = parseInt(image.x*per)
            	var imgy = parseInt(image.y*per)
            	var imgwidth = parseInt(image.img.width*per)
            	var imgheight = parseInt(image.img.height*per)
            if (imgx<=clickX&&clickX<=imgwidth+(imgx-pos.x)&&
                imgy<=clickY&&clickY<=imgheight+(imgy-pos.y))
            {
               map.onclick(image,clickX,clickY);
               break;
            }
           }

        }
	};
}

Map.prototype.loadGameImgs = function(sexImg){
    this.m_imgs = game_imgs;
    this.m_imgs.push(sexImg);
    var map = this;
    this.imgLoaded = 0;
	this.m_imgs.sort(this.sortImg);
	var mapImgs = game_imgs;

    for (var i=0 ; i<mapImgs.length; i++)
    {
        var img = new Image();
        img.src = mapImgs[i].src;
		if (g_screenkey>640)
		{
			if (mapImgs[i].name=="map")
			{
				img.src = "static/img/map"+g_screenkey+".jpg";
			}else if (mapImgs[i].name=="quest"||mapImgs[i].name=="toplist")
			{
				if (g_screenkey==720)
				{
					mapImgs[i].x += 70;
				}else
					mapImgs[i].x += 150;
			}else if (mapImgs[i].name=="help")
			{
				mapImgs[i].x *= SIZEPER;
				mapImgs[i].y *= SIZEPER;
			}
		}
        img.onload=function(){
           // map.draw();
            map.imgLoaded++;
            g_loading.add(3);
            if (map.imgLoaded>=mapImgs.length){
             map.draw();
             g_login.loadGameImgsBack();
            }
        };	       
        mapImgs[i].img = img;
        if (mapImgs[i].hasDiv==true){
            var div = document.createElement("DIV");
            div.id = "tag"+mapImgs[i].name;
            div.className = "cfpage_text "+mapImgs[i].name;
            if (mapImgs[i].zindex)
            	div.style.zIndex = mapImgs[i].zindex;
            document.body.appendChild(div);	
        }        
		g_game.enter = true;
    }
}

Map.prototype.addImg = function(img)
{
    var map = this;
    var img0 = new Image();
    this.m_imgs.push(img);
   this.m_imgs.sort(this.sortImg);
    img0.src = img.src;
    img0.onload=function(){
         //map.draw();
         g_login.loadImgCallback();
    };    
    img.img = img0;
}

Map.prototype.addImg2 = function(img)
{
    var map = this;
    var img0 = new Image();
    this.m_imgs.push(img);
   this.m_imgs.sort(this.sortImg);
    img0.src = img.src;
    img0.onload=function(){
         map.draw();
    };    
    img.img = img0;
}

Map.prototype.removeImg = function(img)
{
	for (var i=0;i<this.m_imgs.length;i++)
	{
		if (this.m_imgs[i].name==img.name){
			this.m_imgs.splice(i,1);
			break;
		}
	}
}

Map.prototype.findImg = function(imgName)
{
	for (var i=0;i<this.m_imgs.length;i++)
	{
		if (this.m_imgs[i].name==imgName){
			return this.m_imgs[i];
		}
	}
}
Map.prototype.sortImg = function(img1,img2){
	if (img1.zindex==null)
		img1.zindex = 1;
	if (img2.zindex==null)
		img2.zindex = 1;
	return (img1.zindex-img2.zindex);
}

Map.prototype.clear = function(){
	var canvas = this.m_canvas;
    var context = canvas.getContext('2d');
   context.clearRect(0,0,canvas.width,canvas.height);
}

Map.prototype.draw = function(){
	var pos = this.m_pos;
	var scale = this.m_imgScale;
	this.clear();
	var canvas = this.m_canvas;
    var context = canvas.getContext('2d');
   for (var i=0 ;i<this.m_imgs.length ;i++ )
   {
  	  var img = this.m_imgs[i].img;
  	  var newx = this.m_imgs[i].x;
  	  var newy = this.m_imgs[i].y;
         if(this.m_imgs[i].abs!=true)
        {
  	     newx = pos.x + this.m_imgs[i].x;
  	     newy = pos.y + this.m_imgs[i].y;
        }
      context.drawImage(img,0,0,img.width,img.height,newx,newy,img.width*scale,img.height*scale);
   }
	if (g_game.drawCallback!=null){
	  var aa = g_game.drawCallback;
	  eval ("(" + aa + "())");
	  g_game.drawCallback = null;
	}
}

Map.prototype.update = function(){
	return;
	if (!g_game.enter) return;
	
	for (var i=0;i<this.m_imgs.length;i++){
		var name = this.m_imgs[i].name;
		if (name=="car3"){
			if (this.m_imgs[i].x<=205){
			 this.m_imgs[i].x = 585;
			 this.m_imgs[i].y = 843;
			}else{
			this.m_imgs[i].x -= 6;
			this.m_imgs[i].y += 4;
			}
		}else if (name=="car4"){
			if (this.m_imgs[i].x<=405){
			 this.m_imgs[i].x = 585;
			 this.m_imgs[i].y = 603;
			}else{
			this.m_imgs[i].x -= 3;
			this.m_imgs[i].y += 2;
			}
		}
	}
	this.draw();
}

Map.prototype.onclick = function(obj,clickX,clickY){
    //log('click map obj: '+obj.name);
    var clickObj = g_game.sys[obj.name];
    if (clickObj!=null)
        clickObj.onclick(clickX,clickY);
    else
    	g_login.onImgClick(obj);
}

Scene = function(canvas){
    this.name = "scene";
	this.m_x = 0;	//scene x
	this.m_y = 0;	//scene y
		
}

Scene.prototype.init = function(canvas){
	this.m_map = new Map(12,canvas);
	this.m_map.init();
}

Game = function(){
	this.name = "game";
	this.currImgCount = 0;
	this.count = 1;
    this.syncDuration = 6;
	this.mm = {"aa":'ss',"bb":'ssb'};
	this.sys = {};
}

Game.prototype = new Datamgr();

Game.prototype.init = function(canvas){
	var tdata = store.get(this.name);
	if (tdata==null)
	{
		var data_game = [{id:1,dataLoaded:true,createtime:Date.parse(new Date())}];
		store.set(this.name,data_game);
	}
	
	this.m_scene = new Scene();
	var scene = this.m_scene;
	scene.init(canvas);
	
	g_loading.set(43);

	var game = this;
	
	setInterval(function(){
	   g_game.update();
	  },UpdateDuration
	);

	  var versions = browser.versions;
	this.register(g_insure);
	this.register(g_title);
	this.register(g_quest);
	this.register(g_playerinfo);
	this.register(g_toplist);
	g_loading.set(47);
	this.register(g_stock);
	this.register(g_bank);
	this.register(g_help);
	this.register(g_login);

	g_loading.set(RES_INIT);
	
	g_login.init();
	
}

Game.prototype.loadGameImgs = function(head_img)
{
 this.imgCount = game_imgs.length+1;
 this.m_scene.m_map.loadGameImgs(head_img);
}

Game.prototype.onEnter = function(){
	g_insure.onEnter();
	g_stock.onEnter();
	
  var bankDiv=document.createElement("div");
  bankDiv.className="cfbank_div";
  bankDiv.innerHTML = "<img class='cficon_bank' src='static/img/icon_bank.png' onclick='g_bank.onclick()'>"
  document.body.appendChild(bankDiv);   

	
  var stockDiv=document.createElement("div");
  stockDiv.className="cfstock_div";
  stockDiv.innerHTML = "<img class='cficon_stock' src='static/img/icon_stock.png' onclick='g_stock.onclick()'>"
  document.body.appendChild(stockDiv);   
	
  var insureDiv=document.createElement("div");
  insureDiv.className="cfinsure_div";
  insureDiv.innerHTML = "<img class='cficon_insure' src='static/img/icon_insure.png' onclick='g_insure.onclick()'>"
  document.body.appendChild(insureDiv);   

  	
carAni1();
carAni2();
carAni3();
	
	this.register(g_event);
}

Game.prototype.register = function(obj){
    this.sys[obj.name] = obj;
}

Game.prototype.addImg2 = function(img,imgCount,drawCallback){
	this.imgCount = imgCount;
    this.m_scene.m_map.addImg2(img);
}

Game.prototype.addImg = function(img,imgCount,drawCallback){
	this.imgCount = imgCount;
	this.drawCallback = drawCallback;
    this.m_scene.m_map.addImg(img);
}

Game.prototype.removeImg = function(img){
    this.m_scene.m_map.removeImg(img);
}
         
Game.prototype.syncData = function(){
	//alert('Game.syncData');
}   
   
//
Game.prototype.update = function(){
    this.count ++;
    
    this.m_scene.m_map.update();
    
   if (this.count%this.syncDuration==0) {
        this.syncData();
    }
	
   for (key in this.sys)
   {
        this.sys[key].update();
   }
}
