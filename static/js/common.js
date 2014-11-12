
var g_dbname = 'cfgame';
var g_dbfile = 'cfgame_file';
var g_dbversion = '1.0';
var g_dbsize = 200000;
var g_game;
   
var g_table_top = 't_toplist';

function errorDB(err){
   alert("Error processing SQL: "+err.code);
}

log = function(text){
	 var console=document.getElementById('console');
	console.innerHTML = text;
	//alert(text);
}
