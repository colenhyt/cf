var quest_type = [
    {name:"insure",type:1},
    {name:"invest",type:2},
]

var ITEM_TYPE_CASH = 0;
var ITEM_TYPE_INSURE = 1;

var data_questdata = [
    {name:"task1",id:1,type:1,desc:"go to get an insure",
    need:[
        {itemtype:ITEM_TYPE_CASH,value:10},
            ],
    cost:[
        {t:1,v:1},
    ],
    data:{relateid:2},
    prize:[
            {itemtype:ITEM_TYPE_CASH,value:100},
           ],
    }
            ,
]