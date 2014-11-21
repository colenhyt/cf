// JavaScript Document
/*=======================================*/

表结构定义 :
/* dbTable is database structure in this example, and contains 2 tables 'foo' and 'boo' */
/* and also the table structure in table properties                                                           */
var game_table_schema = [{
        table : 't_player',
        properties : [
            {name : 'playerid',type : 'INT PRIMARY KEY ASC'}, 
            {name : 'accountid'}, 
            {name : 'playername'}, 
            {name : 'pwd'}, 
            {name : 'version'}
        ]
    }, {
        table : 'boo',
        properties : [{
                name : 'boo_id',
                type : 'INT PRIMARY KEY ASC'
            }, {
                name : 'boo_field_1',
                type : ''
            }, {
                name : 'boo_field_2',
                type : ''
            }
        ]
    }
];
