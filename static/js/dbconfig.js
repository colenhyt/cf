// JavaScript Document
/*=======================================*/

/* dbTable is database structure in this example, and contains 2 tables 'foo' and 'boo' */
/* and also the table structure in table properties                                                           */
var game_table_schema = [
    {
        table : 't_game',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'dataLoaded'}, 
            {name : 'createtime'}, 
        ]
    },
    {
        table : 't_player',
        properties : [
            {name : 'playerid',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'accountid'}, 
            {name : 'playername'}, 
            {name : 'pwd'}, 
            {name : 'exp'}, 
            {name : 'cash'},
            {name : 'quest'}, 
            {name : 'lastsignin'},
            {name : 'sex'}, 
            {name : 'createtime'}, 
            {name : 'version'}
        ]
    }, 
    {
        table : 't_playerlog',
        properties : [
            {name : 'playerid',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'logtime'}, 
            {name : 'feeling'}, 
        ]
    },
    {
        table : 't_signin',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'name'}, 
            {name : 'desc'}, 
            {name : 'day'}, 
            {name : 'type'}, 
            {name : 'value'}, 
        ]
    },
    {
        table : 't_insure',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'type'}, 
            {name : 'name'}, 
            {name : 'price'}, 
            {name : 'profit'}, 
            {name : 'period'}, 
        ]
    },
    {
        table : 't_title',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'level'}, 
            {name : 'name'}, 
            {name : 'exp'}, 
        ]
    },
    {
        table : 't_event',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'type'}, 
            {name : 'value'}, 
        ]
    },
    {
        table : 't_quest',
        properties : [
            {name : 'id',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'name'}, 
            {name : 'type'}, 
            {name : 'desc'}, 
            {name : 'need'}, 
            {name : 'cost'}, 
            {name : 'target'}, 
            {name : 'prize'}, 
        ]
    },
];

