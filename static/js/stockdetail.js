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
	var stock;
	for (var i=0;i<data_quotedata.length;i++){
		if (data_quotedata[i].name==stockName){
			stock = data_quotedata[i];
			break;
		}
	}
	if (stock==null){
		alert('no stock quote for:'+stockName);
		return;
	}
		
	var time = new Date();
	var stockHead = {currPs:123.66,dir:0,per:5.5,currTime:time,status:1};
	var titleText = stock.name+","+stockHead.currPs+":"+stockHead.dir+":("+stockHead.per+")";
	var subTitleText = stockHead.currTime+":s"+stockHead.status+"(北京时间)"
		var flow = [];
		for(var i=0;i<stock.quote.length;i++){
			flow.push(stock.quote[i][2]);
		}
		
		var data = [
		         	{
		         		name : 'PV',
		         		value:flow,
		         		color:'blue',
		         		line_width:1
		         	}
		         ];
        
        var prices = [10,20,30,40,50];
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
			width : 500,
			height : 400,
			tip:{
				enable:true,
				shadow:true,
				listeners:{
					 //tip:提示框对象、name:数据名称、value:数据值、text:当前文本、i:数据点的索引
					parseText:function(tip,name,value,text,i){
						return "<span style='color:#005268;font-size:12px;'>"+labels[i]+":00价格约:<br/>"+
						"</span><span style='color:#005268;font-size:20px;'>"+value+"</span>";
					}
				}
			},
			crosshair:{
				enable:true,
				line_color:'#ec4646'
			},			
			sub_option : {
				smooth : false,
				label:false,
				hollow:false,
				hollow_inside:false,
				point_size:1
			},
			coordinate:{
				width:320,
				height:260,
				striped_factor : 0.18,
				axis:{
					color:'#9d987a',
					width:[0,0,1,1]
				},
				gridHStyle:{
					solid:false,
					size:2,
				},
				gridVStyle:{
					solid:false,
					size:2,
				},
				scale:[{
					 position:'left',	
					 start_scale:0,
					 end_scale:50,
					 scale_space:10,
					 scale_size:2,
					 scale_enable : false,
					 label : {color:'#9d987a',font : '微软雅黑',fontsize:11,fontweight:600},
					 scale_color:'#9f9f9f'
				},{
					 position:'bottom',	
					 label : {color:'#9d987a',font : '微软雅黑',fontsize:11,fontweight:600},
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
					.fillText('价格',x-40,y-12,false,'#9d987a')
					.textBaseline('top')
					.fillText('',x+w+12,y+h+10,false,'#9d987a');
					
				}
		}));
	//开始画图
	chart.draw();
}
var g_stockdetail = new Stockdetail();
g_stockdetail.init();
