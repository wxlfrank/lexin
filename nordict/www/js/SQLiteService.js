var dictionary = null;
function initDB() {
    try {
        appConsole.print("open database!")
        dictionary = window.sqlitePlugin.openDatabase({name: "dict.db", location: 'default'});
        appConsole.print("Open database successfully!")
    } catch (e) {
        appConsole.print("Error happens when opening database!")
        appConsole.print(typeof e);
        appConsole.print(e.message);
    }
}

var DICT = [
    new DictItem("a", "e1"),
    new DictItem("ab", "e1"),
    new DictItem("ac", "e1"),
    new DictItem("ad", "e1"),
    new DictItem("ae", "e1"),
    new DictItem("af", "e1"),
    new DictItem("ag", "e1"),
    new DictItem("ah", "e1")
];


function DictItem(word, explain) {
    this.word = word;
    this.explain = explain;
}
getDictItemFromRecord = function(row)
{
    var result = new DictItem(row["word"], row["explain"]);
    result.sound = row["sound"];
    result.syllabel = row["syllabel"];
    result.clazz = row["clazz"];
    result.format = row["format"];
    result.composite = row["composite"];
    result.alternatives = row["alternatives"];
    result.comment = row["comment"];
    result.examples = row["examples"];
    if(result.examples.length > 0)
        result.examples = result.examples.split("|");
    result.phrases = row["phrases"];
    return result;
};

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
    this.query = function (word, success) {
        //DICT.forEach(function (iter) {
        //    if (iter.word.startsWith(word))
        //        results.push(iter);
        //});
        perform(word, success, function (table) {
            return "select * from " + table + " where word like '" + word + "%' order by word limit 50 ;";
        });
    };
    this.query_exact = function (word, explain, success) {
        //DICT.forEach(function (iter) {
        //    if (iter.word.localeCompare(word) == 0 && iter.explain.localeCompare(explain) == 0)
        //        results.push(iter);
        //});
        perform(word, success, function (table) {
            return "select * from " + table + " where word='" + word + "' and explain='" + explain + "';";
        });
    };
    this.query_suggest = function (word, success) {
        //DICT.forEach(function (iter) {
        //    if (iter.word.startsWith(word))
        //        results.push(iter);
        //});
        //appConsole.print(results.toString());
        perform(word, success, function (table) {
            return "select word, explain from " + table + " where word like '" + word + "%' order by word limit 50;";
        });
    };
});

