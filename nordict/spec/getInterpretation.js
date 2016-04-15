$(document).ready(function(){
    appConsole = new Console($("body>.appConsole:first"));
});
describe("getInterpretation", function(){

    it("getBegge", function(){
        function getTable(word) {
            if (word == null || word.length == 0)
                return null;
            var table = word.substr(0, 1);
            if (table.match(/[a-z]/).length > 0)
                return table;
            return "other";
        }
        var f = function(a){
            a.push("b");
        };
        expect(getTable("jeg").localeCompare("j")).toEqual(0);
        var a = ["a"];
        f(a);
        console.log(a);
    });

    it("getPhrase", function(){
        var pharase = getPhrases(splitToArray('vi tar avstand fra vold ("vi misliker vold")|avstandstaking ("negativ kritikk")'));
        expect(pharase.length).toEqual(2);
        expect(pharase[0][0].length).not.toEqual(0);
        expect(pharase[0][1].length).not.toEqual(0);
    });

    it("test map in javascript", function(){
        var obj = {
            "a b" : "a b"
        };
        obj["c d "] = "c d ";
        var key = "a b";
        expect(obj[key]).not.toBeNull();
        key = "c d ";
        expect(obj[key]).not.toBeNull();
    });

    it("test boolean", function(){
       var c = true;
        if(c){
            expect(true).toEqual(true);
            appConsole.print("true is true");
        }

        c = undefined;
        if(!c){
            expect(true).toEqual(true);
            appConsole.print("undefined is false");
        }
        c = null;
        if(!c){
            expect(true).toEqual(true);
            appConsole.print("null is false");
        }
        c = {};
        if(c){
            expect(true).toEqual(true);
            appConsole.print("obj is true");
        }
    });
});
