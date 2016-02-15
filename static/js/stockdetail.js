Stockdetail = function(){
    this.name = "Stockdetaildetail";
    this.pageCount = 4;
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.tagdetailname = this.tagname+"detail";
    this.pagedetailname = this.tagdetailname+"page";
    this.data = {};
}

Stockdetail.prototype = new Datamgr();

Stockdetail.prototype.init = function(){
	//this.buildHTML();
}

Stockdetail.prototype.drawQuote = function(itemid,currPs,quotes,divName){
	var sdata = store.get(g_stock.name);
	var item = sdata[itemid];
	if (item==null){
		log('no stock quote for:'+itemid);
		return;
	}
	
	if (quotes==null){
		g_msg.tip('no stock quote for:'+itemid);
		return;
	}
		
	var time = new Date();
	var firstPs = quotes[0].price;
	var per = (currPs-firstPs)/firstPs;
	var color = "red"
	var dir = "↑";
	if (per<0) {
		dir = "↓";
		color = "green"
	}
	per = ForDight(per*100,2);
	var now = new Date();
	var tt = now.toLocaleTimeString();
	var status = "已休市"
	if (g_stock.isStockOpen()) {
		status = "开市";
		tt = now.toLocaleTimeString();
	}
	var stockHead = {currPs:currPs,dir:dir,per:per,currTime:tt,status:status};
	var titleText = item.name+" "+stockHead.currPs+" "+stockHead.dir+" ("+stockHead.per+"%)";
	var subTitleText = stockHead.currTime+"(北京时间,"+stockHead.status+")"
	var upps = 0;
	var lowps = 0;
		var flow = [];
		var len = quotes.length
		for(var i=0;i<len;i++){
			var ps = ForDight(quotes[i].price,2);
			if (upps==0)
				upps = ps;
			else if (ps>upps)
				upps = ps;
			if (lowps==0)
				lowps = ps;
			else if (ps<lowps)
				lowps = ps;
			flow.push(ps);
			if (itemid==18)
			 i++;
			
		}
		//alert(flow.length)
		flow.push(currPs);
		if (currPs<lowps)
		 lowps = currPs;
		else if (currPs>upps)
		 upps = currPs;
		 
		var sl_unit = ForDight((upps - lowps)/4,2);
		if (sl_unit>1)
		 sl_unit = parseInt(sl_unit);
		else if (sl_unit<0.5)
		 sl_unit = 0.5;
		lowps = ForDight(lowps,1);
		if (lowps>1)
		 lowps = parseInt(lowps);
		upps += sl_unit;
		upps = ForDight(upps,1);
		if (upps>1)
		 upps = parseInt(upps);
		 var data = [
		         	{
		         		name : 'PV',
		         		value:flow,
		         		color:'blue',
		         		line_width:1
		         	}
		         ];
        
        
		var labels = ["9:00"];
		if (!g_stock.isStockOpen()){
			labels = ["9:00","13:00","17:00","21:00"];
		}else{
        var startDate = new Date();
        startDate.setHours(9);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        
        var split = 4;
        var now = new Date();
        
        var diff = Date.parse(now)-Date.parse(startDate);
        diff = diff/split;
        for (var i=1;i<split+1;i++){
         var date = new Date();
         if (date.getTime()>startDate.getTime()+i*diff)
          date.setTime(startDate.getTime()+i*diff);
         date.setSeconds(0);
         var dateStr = date.getHours()+":";
          if (date.getMinutes()>9)
           dateStr += date.getMinutes();
          else
           dateStr += "0"+date.getMinutes();
         labels.push(dateStr);
        }
		}
		
		var chart = new iChart.LineBasic2D({
			render : divName,
			data: data,
			offsetx: 15,
			offsety: -10,
			align:'center',
			title : {
				text:titleText,
				offsety: -5,
				font : '微软雅黑',
				fontsize:getSizes().StockView[4],
				color:color
			},
			subtitle : {
				text:subTitleText,
				offsety: -5,
				font : '微软雅黑',
				fontsize:getSizes().StockView[4]-2,
				color:color
			},
			animation:true,
			width : getSizes().StockView[0],
			height : getSizes().StockView[1],
			border:{
					enable:false,
				 },
			background_color: "#D9E7EA",
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
						return "<span style='color:#005268;font-size:15px;'>价格约:<br/>"+
						"</span><span style='color:#005268;font-size:20px;'>"+ForDight(value)+"</span>";
					}
				}
			},
			crosshair:{
				enable:true,
				line_color:'#311212'
			},			
			sub_option : {
				smooth : false,
				label:false,
				hollow:false,
				hollow_inside:false,
				point_size:1
			},
			coordinate:{
				width:getSizes().StockView[2],
				height:getSizes().StockView[3],
				striped_factor : 0.18,
				axis:{
					color:'#311212',
					width:[0,0,1,1]
				},
				gridHStyle:{
					solid:false,
					size:2,
					color:'#c0c0c0',
				},
				gridVStyle:{
					solid:false,
					size:2,
					color:'#c0c0c0',
				},
				scale:[{
					 position:'left',	
					 start_scale:lowps,
					 end_scale:upps,
					 scale_space:sl_unit,
					 scale_size:2,
					 scale_enable : false,
					 label : {color:'#311212',font : '微软雅黑',fontsize:getSizes().StockView[4]-3,fontweight:600},
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 label : {color:'#311212',font : '微软雅黑',fontsize:getSizes().StockView[4]-3,fontweight:600},
					 scale_enable : false,
					 labels:labels
				}]
			}
		});
		//利用自定义组件构造左侧说明文本
		chart.plugin(new iChart.Custom({
				drawFn:function(){
					//计算位置
					var coo = chart.getCoordinate(),
						x = coo.get('originx'),
						y = coo.get('originy'),
						w = coo.width,
						h = coo.height;
					//在左上侧的位置，渲染一个单位的文字
					chart.target.textAlign('start')
					.textBaseline('bottom')
					.textFont('600 11px 微软雅黑')
					.fillText('价格',x-20,y-12,false,'#9d987a')
					.textBaseline('top')
					.fillText('',x+w+12,y+h+10,false,'#9d987a');
					
				}
		}));
	//开始画图
	chart.draw();
}
var g_stockdetail = new Stockdetail();

