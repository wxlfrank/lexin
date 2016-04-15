var dictionary = null;
function initDB() {
    dictionary = window.sqlitePlugin.openDatabase({name: "dict.db", location: 'default'}, function(db){
        appConsole.print("Open database successfully!")
        createFavorite(db);
    }, function(e){
        appConsole.print("Error happens when opening database!")
        appConsole.print(e.message);
    });
}

var createFavorite = function(db){
    db.executeSql("create table if not exists 'favorite' (word text not null, explain text not null, primary key(word, explain));", [], function(){
        appConsole.print("create table 'favorite' successfully");
    }, function(e){
        appConsole.print(e.message);
    });
}


function DictItem(word, explain) {
    this.word = word;
    this.explain = explain;
}

var splitToArray = function(str){
    var result = str.split("|");
    if(str.length == 0){
        result.length = 0;
    }
    return result;
}
var getField = function(row, field){
    var result = row[field];
    if(result == null)
        result = '';
    return result;
}
var getPhrases = function(phrases){
    var phraseArray = [];
    for(var iter in phrases){
        var cur = phrases[iter];
        var start = cur.indexOf('("');
        var key = cur.substring(0, start).trim();
        var end = cur.indexOf('")', start);
        if(end == -1) end = undefined;
        var value = cur.substring(start + 2, end);
        phraseArray.push([key, value]);
    }
    return phraseArray;
}
app.service('SQLiteService', function () {
    var getTableName = function (word) {
        if (word == null || word.length == 0)
            return null;
        var table = word.substr(0, 1);
        if (table.match(/[a-z]/).length > 0)
            return table;
        return "other";
    };
    var perform = function (word, success, getSQL) {
        if (dictionary == null) return;
        var table = getTableName(word.toLowerCase());
        if (table == null) return;
        dictionary.executeSql(getSQL(table), [], success, function (e) {
            if (appConsole != null)
                appConsole.print(e.message);
        });
    };
    this.query = function (word, results, success) {
        results.length = 0;
        perform(word, function(res){
            for (var index = 0; index < res.rows.length; ++index) {
                results.push(getDictItemFromRecord(res.rows.item(index)));
            }
            if(!!success)
                success();
        }, function (table) {
            return "select * from '" + table + "' where word like '" + word + "%' and explain <> '' order by word limit 50;";
        });
    };
    this.query_exact = function (word, explain, results, success) {
        results.length = 0;
        perform(word, function(res){
            for (var index = 0; index < res.rows.length; ++index) {
                results.push(getDictItemFromRecord(res.rows.item(index)));
            }
            if(!!success)
                success();
        }, function (table) {
            return "select * from '" + table + "' where word='" + word + "' and explain='" + explain + "';";
        });
    };
    this.query_suggest = function (word, results, success) {
        results.length = 0;
        perform(word, function(res){
            for (var index = 0; index < res.rows.length; ++index) {
                var row = res.rows.item(index);
                results.push(new DictItem(getField(row, "word"), getField(row, "explain")));
            }
            if(!!success)
                success();
        }, function (table) {
            return "select word, explain from '" + table + "' where word like '" + word + "%' and explain <> '' order by word limit 50;";
        });
    };
    this.queryFavorite = function (word, results, success) {
        perform(word, function(res){
            var favorites = {};
            for(var index = 0; index < res.rows.length; ++index){
                var row = res.rows.item(index);
                favorites[getField(row, "word") + "|" + getField(row, "explain")] = true;
            }
            var favorite;
            for(var index in results){
                var item = results[index];
                item.favorite = !!(favorites[item.word + "|" + item.explain]);
            }
            if(!!success)
                success();
        }, function (table) {
            return "select * from 'favorite' where word like '" + word + "%' order by word limit 50;";
        });
    };
    this.deleteFavorite = function(item, success){
        perform(item.word, success, function (table) {
            return "delete from 'favorite' where word='" + item.word + "' and explain='" + item.explain + "';";
        });
    };
    this.addFavorite = function(item, success){
        perform(item.word, success, function (table) {
            return "insert into 'favorite' values('" + item.word + "', '" + item.explain + "');";
        });
    };
});

getDictItemFromRecord = function(row)
{
    try {
        var result = new DictItem(getField(row, "word"), getField(row, "explain"));
        result.sound = getField(row, "sound");
        result.syllabel = getField(row, "syllabel");
        result.clazz = getField(row, "clazz");
        result.format = getField(row, "format").replace(' ]', ']');
        result.composite = splitToArray(getField(row, "composite"));
        result.composite.forEach(function (iter, index, arr) {
            var i = iter.indexOf(') ');
            if (i != -1)
                arr[index] = iter.substring(i + 2);
        });
        result.alternatives = getField(row, "alternatives");
        result.comment = getField(row, "comment");
        result.examples = splitToArray(getField(row, "examples"));
        var phrases = splitToArray(getField(row, "phrases"));
        result.phrases = getPhrases(phrases);
        return result;
    }catch (e){
        appConsole.print(e.message);
    }
};

