// JavaScript Document
Signin = function(){
}

Signin.prototype = new Datamgr();

Signin.prototype.init = function(){

}

Signin.prototype.start = function(lastsignin){
    //alert('sign start'+lastsignin);
    
    //update login time:
}

Signin.prototype.updateLogin = function(){
    var  lastsignin = {'name':'lastsignin','value':Date.parse(new Date())};
    g_player.updateData(lastsignin); 
}

var g_signin = new Signin();
g_signin.init();
