
Map = function(mapId,canvas){
    this.name = "map";
	this.m_pos = {x:0,y:0};
	this.m_imgScale = 1;
	this.m_canvas = canvas;
	this.m_mapId = mapId;
	this.m_mapImg = null;
	this.m_imgs = [];
}

Map.prototype.init = function(width,height,mapFile){
	this.m_width = width;
	this.m_height = height;
	this.m_mapFile = mapFile;
    this.m_imgs = game_imgs;
	var map = this;
	var canvas = this.m_canvas;
	var pos = this.m_pos;
	var mapImg;
	var mapImgs = this.m_imgs;

    for (var i=0 ; i<mapImgs.length; i++)
    {
        var img = new Image();
        img.src = mapImgs[i].src;
        img.onload=function(){
            map.draw();
        };	       
        mapImgs[i].img = img;
        if (mapImgs[i].name=="map"){
            mapImg = img;
        }
        
        if (mapImgs[i].hasDiv==true){
            var div = document.createElement("DIV");
            div.id = "tag"+mapImgs[i].name;
            div.style.position = "absolute";
            if (mapImgs[i].divX>0)
                div.style.left = mapImgs[i].divX+"px";
            else 
                div.style.left = mapImgs[i].x + "px";
            
            if (mapImgs[i].divY>0) 
                div.style.top = mapImgs[i].divY+"px";
            else
                div.style.top = mapImgs[i].y + "px";
            //div.style = "position:absolute;left:100px,top:300px";           
            var cc = document.getElementById("divhide");
            cc.insertBefore(div);
        }        
    }

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
		for (var i=0 ; i<mapImgs.length;i++ )
        {
            var image = mapImgs[i];
            if (image.name!="map")
            {            
            
            if (image.x<=clickX&&clickX<=image.img.width+(image.x-pos.x)&&
                image.y<=clickY&&clickY<=image.img.height+(image.y-pos.y))
            {
               map.onclick(image,clickX,clickY);
               break;
            }
           }

        }
	};

	canvas.onmousewheel = function(e){
		alert('onmousewheel');
	};

	canvas.onmousedown = function(event){
        canvas.style.cursor="move";
		var old_pos = windowToCanvas(event.clientX,event.clientY);

		canvas.onmousemove=function(event){
			var curr_pos = windowToCanvas(event.clientX,event.clientY);
			var x=curr_pos.x-old_pos.x;
			var newx = pos.x+x;
			var y=curr_pos.y-old_pos.y;
			var newy = pos.y+y;
			var newpos = resetPos(newx,newy);
			if (newpos.x!=pos.x||newpos.y!=pos.y)
			{
				pos.x = newpos.x;
				pos.y = newpos.y;
				old_pos = curr_pos;
				map.draw();
			}
		};

	   canvas.onmouseup = function(){
			canvas.onmousemove = null;
			canvas.onmouseup = null;
			canvas.style.cursor = "default";
		};
	};

	canvas.onmousewheel = canvas.onwheel = function(event){
		var old_pos = windowToCanvas(event.clientX,event.clientY);
		event.wheelDelta=event.wheelDelta?event.wheelDelta:(event.deltaY*(-40));
		if(event.wheelDelta>0){
			map.m_imgScale*=2;
			pos.x=pos.x*2-old_pos.x;
			pos.y=pos.y*2-old_pos.y;
		}else{
			map.m_imgScale/=2;
			//log(map.m_mapImg.width);
			pos.x=pos.x*0.5+old_pos.x*0.5;
			pos.y=pos.y*0.5+old_pos.y*0.5;
		}
		map.draw();
	};
}

Map.prototype.addImg = function(img)
{
    var map = this;
    var img0 = new Image();
    img0.src = img.src;
    img0.onload=function(){
         map.draw();
    };    
    img.img = img0;
    this.m_imgs.push(img);
}

Map.prototype.draw = function(){
	var pos = this.m_pos;
	var scale = this.m_imgScale;
	var canvas = this.m_canvas;
    var context = canvas.getContext('2d');
   context.clearRect(0,0,canvas.width,canvas.height);
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

}

Map.prototype.onclick = function(obj,clickX,clickY){
    log('click map obj: '+obj.name);
    var clickObj = g_game.sys[obj.name];
    if (clickObj!=null)
        clickObj.onclick(clickX,clickY);
}

Scene = function(canvas){
    this.name = "scene";
	this.tt = 33;	
	this.m_x = 0;	//scene x
	this.m_y = 0;	//scene y
		
}

Scene.prototype.init = function(canvas,width,height){
	this.m_width = width;
	this.m_height = height;
	this.m_map = new Map(12,canvas);
	this.m_map.init(width,height,"static/img/map.jpg");
}

Scene.prototype.draw = function(){
}

Scene.prototype.update = function(){
}

Game = function(name){
	this.name = name;
	this.tt = 1;
	this.mm = {"aa":'ss',"bb":'ssb'};
	this.sys = {};
}

Game.prototype.init = function(canvas){
	
	this.init_db();
	
	this.m_scene = new Scene();
	var scene = this.m_scene;
	scene.init(canvas,640,960);
	
	var game = this;
	
	TimerUpdate = function(){
	   game.update();
	};
	
	setInterval(TimerUpdate,1000);
}

Game.prototype.init_db = function(){
    g_db = new cDB({
        _db : window.openDatabase(g_dbname, g_dbversion, g_dbfile , g_dbsize)
    });
    
   //g_db.dropTable('t_player');
    //g_db.dropTable('t_signin');
   
    if (!g_db.check(game_table_schema)) {
        g_db = false;
        alert('Failed to cennect to database.');
    }
	
	log('init db ss');
	
}

//主角注册后客户端初始化:
Game.prototype.init_client = function() {
    //1. 装载第一版js数据到sql
   g_db.insertJson(game_tables["signin"], data_signindata, function () {
     alert('insertion done');
    });
    return true;
}

Game.prototype.register = function(obj){
    this.sys[obj.name] = obj;
}

//
Game.prototype.update = function(){
    this.tt += 1;
   // log(this.m_name+ this.tt);
   for (key in this.sys)
   {
        this.sys[key].update();
   }
}
