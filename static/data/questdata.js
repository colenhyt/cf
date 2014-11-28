var quest_type = [
    {name:"insure",type:1},
    {name:"invest",type:2},
]

var ITEM_TYPE_CASH = 0;
var ITEM_TYPE_INSURE = 1;

var data_questdata = [
    {name:"买个保险",id:1,type:1,desc:"随便买个保险",
    need:[
        {t:ITEM_TYPE_CASH,v:10},
            ],
    cost:[
        {t:1,v:1},
    ],
    target:[],
    prize:[
            {t:ITEM_TYPE_CASH,v:100},
           ],
    },
    {name:"买个股票",id:2,type:2,desc:"蓝筹股",
    need:[
        {t:ITEM_TYPE_CASH,v:30},
            ],
    cost:[
        {t:1,v:1},
    ],
    target:[],
    prize:[
            {t:ITEM_TYPE_CASH,v:120},
           ],
    },
    {name:"买个平安股票",id:3,type:2,desc:"平安股票",
    need:[
        {t:ITEM_TYPE_CASH,v:30},
            ],
    cost:[
        {t:1,v:1},
    ],
    target:[
    	{relateid:2},
    ],
    prize:[
            {t:ITEM_TYPE_CASH,v:120},
           ],
    },
]