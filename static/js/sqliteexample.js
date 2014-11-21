// JavaScript Document

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
