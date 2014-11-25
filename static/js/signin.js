// JavaScript Document
Signin = function(){
}

Signin.prototype = new Datamgr();

Signin.prototype.init = function(){

}

Signin.prototype.start = function(startDay){ 
    var signin = this;    
    var query = 'SELECT * FROM t_signin where day>='+startDay;
    g_db.query(query, function (tx, res) {
       if (res.rows.length) {
            //alert('found ' + res.rows.length + ' record(s)');
        } else {
            alert('table foo is empty');
        }
            signin.data = res.rows;
            signin.show();
    });     
}

Signin.prototype.show = function(){
    var tagfeel = document.getElementById("signin_feeling");
    
    for (var i=0;i<feeling_imgs.length;i++)
    {
        var imgData = feeling_imgs[i];
        var div = document.createElement("div");
        div.id = "divfeeling_"+this.id;
        var img = new Image();
        img.src = imgData.src;
        img.id = "imgfeeling_"+imgData.id;
        img.onclick = function(){          
            signin.feeling = this.id;
            this.style.border = "1px red";
        }
        //div.appendChild(img);    
        tagfeel.appendChild(img);       
    }
    
    var options = {
        keyboard : false,
        show     : true
    };	
    $('#mysignin').modal(options);       
}

Signin.prototype.doSignin = function(){
    alert(this.feeling);   
}

Signin.prototype.updateLogin = function(){
    var  lastsignin = {'name':'lastsignin','value':Date.parse(new Date())};
    g_player.updateData(lastsignin); 
}

var g_signin = new Signin();
g_signin.init();
