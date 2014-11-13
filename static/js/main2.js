var canvas,context;
var map,img2,
    imgX2=90,
    imgY2=220;
var img,//图片对象
    imgIsLoaded,//图片是否加载完成;
    imgX=0,
    imgY=0,
    imgScale=1;
//var b1 = {10,10};
var div1;


(function int(){
    canvas=document.getElementById('canvas');
	div1=document.getElementById('b1');
    context=canvas.getContext('2d');
    loadImg();
})();

function aaa(){
	div1.style.left = "150px";
	log(div1.style.left);
	var tt=document.getElementById('b2222');
	tt.style.z-index = 30;
}

function aaa2(){
	log("aaa2");
}

function loadImg(){
    img=new Image();
    img.onload=function(){
        imgIsLoaded=true;
        drawImage();
    }
    img.src="static/img/map.jpg";
    img2=new Image();
    img2.onload=function(){
        imgIsLoaded=true;
        drawImage();
    }
    img2.src="static/img/baozi.png";
}

function drawImage(){
    context.clearRect(0,0,canvas.width,canvas.height);
    context.drawImage(img,0,0,img.width,img.height,imgX,imgY,img.width*imgScale,img.height*imgScale);
    context.drawImage(img2,0,0,img2.width,img2.height,imgX2,imgY2,img2.width*imgScale*0.3,img2.height*imgScale*0.3);
}

canvas.onmousedown=function(event){
    var pos=windowToCanvas(canvas,event.clientX,event.clientY);
    canvas.onmousemove=function(event){
        canvas.style.cursor="move";
        var pos1=windowToCanvas(canvas,event.clientX,event.clientY);
        var x=pos1.x-pos.x;
        var y=pos1.y-pos.y;
        pos=pos1;
        imgX+=x;
        imgY+=y;
        imgX2+=x;
        imgY2+=y;
		div1.style.left+=x;
		div1.style.top+=y;
		drawImage();
    }
    canvas.onmouseup=function(){
        canvas.onmousemove=null;
        canvas.onmouseup=null;
        canvas.style.cursor="default";
    }
}
canvas.onmousewheel=canvas.onwheel=function(event){
    var pos=windowToCanvas(canvas,event.clientX,event.clientY);
    event.wheelDelta=event.wheelDelta?event.wheelDelta:(event.deltaY*(-40));
    if(event.wheelDelta>0){
        imgScale*=2;
        imgX=imgX*2-pos.x;
        imgY=imgY*2-pos.y;
    }else{
        imgScale/=2;
        imgX=imgX*0.5+pos.x*0.5;
        imgY=imgY*0.5+pos.y*0.5;
    }
    drawImage();
}

function windowToCanvas(canvas,x,y){
    var bbox = canvas.getBoundingClientRect();
    return {
        x:x - bbox.left - (bbox.width - canvas.width) / 2,
        y:y - bbox.top - (bbox.height - canvas.height) / 2
    };
}
