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

Stockdetail.prototype.drawQuote = function(stockName,divName){
	var item;
	var sdata = store.get(g_stock.name);
	for (var i=0;i<sdata.length;i++){
		if (sdata[i].name==stockName){
			item = sdata[i];
			break;
		}
	}
	if (item==null){
		log('no stock quote for:'+stockName);
		return;
	}
	
	var quotes = g_stock.findQuotes(item.id);
	if (quotes==null){
		alert('no stock quote for:'+stockName);
		return;
	}
		
	var time = new Date();
	var currPs = quotes[quotes.length-1].price;
	var stockHead = {currPs:currPs,dir:0,per:item.per,currTime:item.createtime,status:1};
	var titleText = item.name+","+stockHead.currPs+":"+stockHead.dir+":("+stockHead.per+")";
	var subTitleText = stockHead.currTime+":s"+stockHead.status+"(北京时间)"
	var upps = 0;
	var lowps = 0;
		var flow = [];
		for(var i=0;i<quotes.length;i++){
			var ps = quotes[i].price;
			if (upps==0)
				upps = ps;
			else if (ps>upps)
				upps = ps;
			if (lowps==0)
				lowps = ps;
			else if (ps<lowps)
				lowps = ps;
			flow.push(ps);
		}
		var diff = upps - lowps;
		var unit = diff/quotes.length;
		unit *= 3;
		if (lowps>unit)
			lowps -= unit;
		lowps = parseInt(lowps);
		upps += unit;
		upps = parseInt(upps);
		var data = [
		         	{
		         		name : 'PV',
		         		value:flow,
		         		color:'blue',
		         		line_width:1
		         	}
		         ];
        
		var labels = ["9:30","10:30","11:30","13:00","14:00","15:00"];
		
		var chart = new iChart.LineBasic2D({
			render : divName,
			data: data,
			align:'center',
			title : {
				text:titleText,
				font : '微软雅黑',
				fontsize:18,
				color:'#b4b4b4'
			},
			subtitle : {
				text:subTitleText,
				font : '微软雅黑',
				fontsize:12,
				color:'#b4b4b4'
			},
			animation:true,
			offsetx: 15,
			width : 540,
			height : 420,
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
						return "<span style='color:#005268;font-size:15px;'>"+labels[i]+":00价格约:<br/>"+
						"</span><span style='color:#005268;font-size:20px;'>"+value+"</span>";
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
				width:480,
				height:280,
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
					 scale_space:2,
					 scale_size:2,
					 scale_enable : false,
					 label : {color:'#311212',font : '微软雅黑',fontsize:15,fontweight:600},
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 label : {color:'#311212',font : '微软雅黑',fontsize:15,fontweight:600},
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
g_stockdetail.init();
