
function testajax(args) {
    htmlobj = $.ajax({url:"../bbs/record.php",async:false});
    $("#abc").html(htmlobj.responseText);
   //onDeviceReady();

}
 
   function populateDB(tx){
	   tx.executeSql('DROP TABLE IF EXISTS test1');
	   tx.executeSql('CREATE TABLE IF NOT EXISTS test1(id unique,name)');
	   tx.executeSql('INSERT INTO test1(id,name)VALUES(1,"tracy1")');
	   tx.executeSql('INSERT INTO test1(id,name)VALUES(2,"colen2")');
	   tx.executeSql('INSERT INTO test1(id,name)VALUES(3,"xtudou333")');
   }
   
   function queryDB(tx){
	   tx.executeSql('SELECT * FROM test1',[],querySuccess,errorDB);
   }
   
   function querySuccess(tx,results){
	   var len = results.rows.length;
	   var status = document.getElementById("status");
	   var string = "Rows: " + len + "<br/>";
	   for(var i = 0;i<len;i++){
		   string += results.rows.item(i).name + "<br/>";
	   }
	   status.innerHTML = string;
   }
   
   function errorDB(err){
	   alert("Error processing SQL: "+err.code);
   }
   
   function successDB(){
	   var db = window.openDatabase("Test","1.0","Test111",200000);
	   db.transaction(queryDB,errorDB);
	   alert('alert successDB');
   }
   
   function onDeviceReady(){
	   var db = window.openDatabase("Test","1.0","Test111",200000);
	   db.transaction(populateDB,errorDB,successDB);
	   //alert('alert');
   }
   