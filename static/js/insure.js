Insure = function(){
    this.name = "insure";
}

Insure.prototype = new Datamgr();

Insure.prototype.init = function(){
    g_game.register(this);
    //store.enable();
    store.set('colen11','rrrrr5555');
}

Insure.prototype.loadDetail = function(id){
        alert(store.get('colen11'));
}

Insure.prototype.showDetail = function(id){
    
        this.loadDetail(id);
        var options = {
            keyboard : false,
            show     : true
        };
	$('#myinsure_detail').modal(options);
}

var g_insure = new Insure();
g_insure.init();