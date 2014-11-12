
function gameInit()
{
	gameInit_db();
}



function gameInit_db(){
 
	function createDB(tx){
		tx.executeSql('CREATE TABLE IF NOT EXISTS t_toplist(id unique,type,top,name,score)');
	}
   
	function initDB_success(){
		//alert('init successful');
	}

	var db = window.openDatabase(g_dbname,g_dbversion,g_dbfile,g_dbsize);
	db.transaction(createDB,errorDB,initDB_success);
}

//gameInit()

Map = function(mapId,canvas){
	this.m_pos = {x:0,y:0};
	this.m_imgScale = 1;
	this.m_canvas = canvas;
	this.m_mapId = mapId;
}

Map.prototype.init = function(width,height,mapFile){
	this.m_width = width;
	this.m_height = height;
	this.m_mapFile = mapFile;
	this.m_mapImg = new Image();
	var map = this;
	var canvas = this.m_canvas;
	var img = this.m_mapImg;
	var pos = this.m_pos;
    img.onload=function(){
        map.draw();
      //  drawImage();
    };	
	img.src = mapFile;


	windowToCanvas = function(x,y){
		var bbox = canvas.getBoundingClientRect();
		return {
			x:x - bbox.left - (bbox.width - canvas.width) / 2,
			y:y - bbox.top - (bbox.height - canvas.height) / 2
		};
	};

	canvas.onclick = function(e){
		//alert('scene cavas click111');
	};

	canvas.onmousewheel = function(e){
		alert('onmousewheel');
	};

	canvas.onmousedown = function(event){
        canvas.style.cursor="move";
		var old_pos = windowToCanvas(event.clientX,event.clientY);

		canvas.onmousemove=function(event){
			var new_pos = windowToCanvas(event.clientX,event.clientY);
			var x=new_pos.x-old_pos.x;
			var y=new_pos.y-old_pos.y;
			old_pos=new_pos;
			pos.x+=x;
			pos.y+=y;
			map.draw();
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
			pos.x=pos.x*0.5+old_pos.x*0.5;
			pos.y=pos.y*0.5+old_pos.y*0.5;
		}
		map.draw();
	};
}

Map.prototype.draw = function(){
    context = this.m_canvas.getContext('2d');
	var img = this.m_mapImg;
	var canvas = this.m_canvas;
   context.clearRect(0,0,canvas.width,canvas.height);
    context.drawImage(img,0,0,img.width,img.height,this.m_pos.x,this.m_pos.y,img.width*this.m_imgScale,img.height*this.m_imgScale);
}

Scene = function(canvas){
	this.m_canvas = canvas;
	this.tt = 33;	
	this.m_x = 0;	//scene x
	this.m_y = 0;	//scene y
		
}

Scene.prototype.init = function(width,height){
	this.m_width = width;
	this.m_height = height;
	this.m_map = new Map(12,this.m_canvas);
	this.m_map.init(width,height,"static/img/map.jpg");
}

Scene.prototype.draw = function(){
}

Scene.prototype.update = function(){
}

Game = function(name){
	this.m_name = name;
	this.mm = {"aa":'ss',"bb":'ssb'};
}

Game.prototype.init = function(canvas){
	this.m_scene = new Scene(canvas);
	var scene = this.m_scene;
	scene.init(640,960);
	log(scene.tt);
}
