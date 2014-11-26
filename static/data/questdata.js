var quest_type = [
    {name:"insure",type:1},
    {name:"invest",type:2},
]

var ITEM_TYPE_CASH = 0;
var ITEM_TYPE_INSURE = 1;

var data_questdata = [
    {name:"task1",id:1,type:1,desc:"go to get an insure",
    need:[
        {t:ITEM_TYPE_CASH,v:10},
            ],
    cost:[
        {t:1,v:1},
    ],
    target:{relateid:2},
    prize:[
            {t:ITEM_TYPE_CASH,v:100},
           ],
    }
            ,
]