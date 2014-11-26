// JavaScript Document
/*=======================================*/

/* dbTable is database structure in this example, and contains 2 tables 'foo' and 'boo' */
/* and also the table structure in table properties                                                           */
var game_table_schema = [
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
        table : 't_event',
        properties : [
            {name : 'eventid',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'type'}, 
            {name : 'value'}, 
        ]
    },
];
