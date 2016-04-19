$(document).ready(function(){
    appConsole = new Console($("body>.appConsole:first"));
});

function getSelected() {
    var text = "";
    if (window.getSelection
        && window.getSelection().toString()
        && $(window.getSelection()).attr('type') != "Caret") {
        text = window.getSelection();
        return text;
    }
    else if (document.getSelection
        && document.getSelection().toString()
        && $(document.getSelection()).attr('type') != "Caret") {
        text = document.getSelection();
        return text;
    }
    else {
        var selection = document.selection && document.selection.createRange();

        if (!(typeof selection === "undefined")
            && selection.text
            && selection.text.toString()) {
            text = selection.text;
            return text;
        }
    }
    return "";
}
function showSelection(event){
    var select = document.getSelection();
    t = getSelected().toString();
    event.cancelBubble = true;
    var button =  $("#searchbutton");
    if(t.length>0) {
        button.css("top", event.clientY);
        button.css("left", event.clientX);
        button.show();
    }else{
        button.hide();
    }
}
$(document).mouseup (function(event){
    showSelection(event);
});
//document.addEventListener('mouseup', showSelection);
//if (!document.all) document.captureEvents(Event.MOUSEUP);
describe("getInterpretation", function(){

    it("test getTableName", function(){
        function getTable(word) {
            if (word == null || word.length == 0)
                return null;
            var table = word.substr(0, 1);
            if(!!(table.match(/[a-z]/)))
                return table;
            return "other";
        }
        expect(getTable("jeg").localeCompare("j")).toEqual(0);
        expect(getTable("å").localeCompare("other")).toEqual(0);
    });

    it("test function reference", function(){
        var f = function(a){
            a.push("b");
        };
        var a = ["a"];
        var length = a.length;
        f(a);
        expect(a.length).toEqual(length + 1);
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
