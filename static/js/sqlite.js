// JavaScript Document
/*************************************/
/* Helman, heldes.com      */
/* helman at heldes dot com    */
/* sqlitedb.js           */
/* SQLite Database Class For HTML5 */
/*************************************/

function cDB(confs) {
    var ret = {
        _db : null,
        _response : null,
        _error : null,
        check : function (tbl) {
            if (!this._db)
                return false;
            var _sql = '',
            _sqlField = '',
            _field = [];
            
            for (var i = 0; i < tbl.length; i++) {
                _sql = "CREATE TABLE IF NOT EXISTS " + tbl[i].table + " (";
                _field = tbl[i].properties;
                _sqlField = '';
                
                for (var j = 0; j < _field.length; j++) {
                    _sqlField += ',`' + _field[j].name + '` ' + _field[j].type;
                }
                
                _sql += _sqlField.substr(1) + ");";
                
                this.query(_sql, null, null, null);
            }
            
            return true;
        },
        getResult : function () {
            return this._response;
        },
        getError : function () {
            return this._error;
        },
        callback_error : function (tx, _er) {
            var err = '';
            if (typeof(tx) == 'object') {
                for (var q in tx) {
                    err += q + ' = "' + tx[q] + '"; ';
                }
            } else {
                err += tx + '; ';
            }
            if (typeof(_er) == 'object') {
                for (var q in _er) {
                    err += q + ' = "' + _er[q] + '"; ';
                }
            } else if (typeof(_er) == 'undefined') {
                err += _er + '; ';
            }
            console.log(err);
            //if(callback) callback();
            return false;
        },
        query : function (sql, callback, params, er) {
            if (!this._db)
                return false;
            var self = this;
            function _er(tx, __er) {
                __er = jQuery.extend(__er, {
                        sql : sql
                    });
                if (er)
                    er(tx, __er);
                else
                    self.callback_error(tx, __er);
            };
            this._db.transaction(function (tx) {
                tx.executeSql(sql, (params ? params : []), callback, _er);
            }, _er);
        },
        update : function (tbl, sets, clauses, callback) {
            var __sql = 'UPDATE ' + tbl,
            _field = null,
            __set = '',
            __clause = '',
            __values = [];
            
            for (var i = 0; i < sets.length; i++) {
                0
                _field = sets[i];
                for (var j = 0; j < _field.length; j++) {
                    __set += ',`' + _field[j].name + '`=?';
                    __values.push(_field[j].value);
                }
            }
            
            for (var i = 0; i < clauses.length; i++) {
                __clause += ',`' + clauses[i].name + '`=?';
                __values.push(clauses[i].value);
            }
            __sql += ((__set != '') ? ' SET ' + __set.substr(1) : '') + ((__clause != '') ? ' WHERE ' + __clause.substr(1) : '') + ';';
            this.query(__sql, callback, __values);
            return true;
        },
        remove : function (tbl, clauses) {
            var __sql = 'DELETE FROM ' + tbl,
            __clause = '';
            
            for (var i = 0; i < clauses.length; i++)
                __clause += ',`' + clauses[i].name + '`="' + escape(clauses[i].value) + '"';
            
            __sql += ' WHERE ' + ((__clause != '') ? __clause.substr(1) : 'FALSE') + ';';
            
            this.query(__sql);
            return true;
        },
        multiInsert : function (tbl, rows, callback, er) {
            if (!this._db)
                return false;
            var self = this;
            var __sql = '',
            _field = null,
            __field = '',
            __qs = [],
            __values = [];
            
            this._db.transaction(function (tx) {
                for (var i = 0; i < rows.length; i++) {
                    __qs = [];
                    __values = [];
                    __field = '';
                    _field = rows[i];
                    
                    for (var j = 0; j < _field.length; j++) {
                        __field += ',`' + _field[j].name + '`';
                        __qs.push('?');
                        __values.push(_field[j].value);
                    }
                    tx.executeSql('INSERT INTO ' + tbl + ' (' + __field.substr(1) + ') VALUES(' + __qs.join(',') + ');', __values, function () {
                        return false;
                    }, (er ? er : self.callback_error));
                }
            }, self.callback_error, function () {
                if (callback)
                    callback();
                return true;
            });
            return true;
        },
        insert : function (tbl, rows, callback) {
            var __sql = '',
            _field = null,
            __field = '',
            __qs = [],
            __values = [],
            __debug = '';
            
            for (var i = 0; i < rows.length; i++) {
                __qs = [];
                __field = '';
                _field = rows[i];
                
                __debug += _field[0].name + ' = ' + _field[0].value + ';';
                for (var j = 0; j < _field.length; j++) {
                    __field += ',`' + _field[j].name + '`';
                    __qs.push('?');
                    __values.push(_field[j].value);
                }
                __sql += 'INSERT INTO ' + tbl + ' (' + __field.substr(1) + ') VALUES(' + __qs.join(',') + ');';
            }
            this.query(__sql, callback, __values);
            return true;
        },
        insertReplace : function (tbl, rows, debug) {
            var __sql = '',
            _field = null,
            __field = '',
            __qs = [],
            __values = [],
            __debug = '';
            
            for (var i = 0; i < rows.length; i++) {
                __qs = [];
                __field = '';
                _field = rows[i];
                
                __debug += _field[0].name + ' = ' + _field[0].value + ';';
                for (var j = 0; j < _field.length; j++) {
                    __field += ',`' + _field[j].name + '`';
                    __qs.push('?');
                    __values.push(_field[j].value);
                }
                __sql += 'INSERT OR REPLACE INTO ' + tbl + ' (' + __field.substr(1) + ') VALUES(' + __qs.join(',') + ');';
            }
            this.query(__sql, null, __values);
            return true;
        },
        dropTable : function (tbl, callback) {
            var __sql = '';
            if (tbl == null)
                return false;
            __sql = 'DROP TABLE IF EXISTS ' + tbl;
            this.query(__sql, callback);
            return true;
        }
    }
    return jQuery.extend(ret, confs);
}















/*=======================================*/
使用方法：
/*=======================================*/

/*=======================================*/
创建数据库：
/* Create or open database with 'websiteDB' as database name and 'website DB' as title, and database site is 5MB */
/* I'm not using 1024 for the size multiplying because i don't want to be near at the margin size                          */
var db = new cDB({
        _db : window.openDatabase("websiteDB", "", "website DB"; , 5 * 1000 * 1000)
    });

/*=======================================*/
建表 :
/* dbTable is database structure in this example, and contains 2 tables 'foo' and 'boo' */
/* and also the table structure in table properties                                                           */
var dbTable = [{
        table : 'foo',
        properties : [{
                name : 'foo_id',
                type : 'INT PRIMARY KEY ASC'
            }, {
                name : 'foo_field_1',
                type : ''
            }, {
                name : 'foo)field_2',
                type : ''
            }
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

/* this line is checking if the database exist or not and then create the database structure.  */
/* table will be created if the table is not exist yet, if the table already exist, it will skip the */
/* table and continue with others tables                                                                                  */
if (!db.check(dbTable)) {
    db = false;
    alert('Failed to cennect to database.');
}

/*=======================================*/
删除表：
db.dropTable('foo');

/*=======================================*/
插入数据：
var row = [];
row.push([{
            'name' : 'foo_id',
            'value' : 1
        }, {
            'name' : 'foo_field_1',
            'value' : 'value 1 field_1'
        }, {
            'name' : 'foo_field_2',
            'value' : 'value 1 field_2']
        }
    ]);
db.insert('foo', row);

插入多行记录：
/*
SQLite is not accepting more than 1 line statement,
that is the reason why we not able to do more than one statement query, like insertion.
If you want to insert more than 1 record at the time, you need to use this function.
 */
var rows = [];
rows.push([{
            'name' : 'boo_id',
            'value' : 1
        }, {
            'name' : 'boo_field_1',
            'value' : 'value 1 field_1'
        }, {
            'name' : 'boo_field_2',
            'value' : 'value 1 field_2']
        }
    ]);
rows.push([{
            'name' : 'boo_id',
            'value' : 2
        }, {
            'name' : 'boo_field_1',
            'value' : 'value 2 field_1'
        }, {
            'name' : 'boo_field_2',
            'value' : 'value 2 field_2']
        }
    ]);
db.multiInsert('boo', rows, function () {
    alert('insertion done');
});

/*
如果想合并insert 和 multiInsert两个函数，可以按下面的方法增加一个判断来处理
 */

if (rows.length >= 2) {
    db.multiInsert('boo', rows, function () {
        alert('insertion done');
    });
} else {
    db.insert('boo', rows);
}

/*=======================================*/
删除数据：
db.remove('boo', [{
            'name' : 'boo_id',
            'value' : 1
        }
    ])

/*=======================================*/
更新数据
db.update('boo', [[{
                'name' : 'boo_id',
                'value' : 2
            }, {
                'name' : 'boo_field_1',
                'value' : 'boo value'
            }
        ]], ['name' : 'boo_id', 'value' : 2])

/*=======================================*/
查询
var query = 'SELECT * FROM foo';
db.query(query, function (tx, res) {
    if (res.rows.length) {
        alert('found ' + res.rows.length + ' record(s)');
    } else {
        alert('table foo is empty');
    }
});
