function onDeviceReady() {
    appConsole.print("Device is ready!");
    downloadDB();
    appConsole.print("Download database!")
}
document.addEventListener("deviceReady", onDeviceReady, false);
/*var getSelected = function () {
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
};*/
//var searchbutton = null;
//var select = "";
function onDOMReady() {
    appConsole = new Console($("body>.appConsole:first"));
    /*searchbutton = $("#searchbutton");
    function showSelection(event){
        appConsole.print("select")
        select = getSelected().toString();
        if(select.length == 0) {
            searchbutton.hide();
            return;
        };
        appConsole.print('"' + select + '"');
        appConsole.printObj(event);
        appConsole.printObj(event.originalEvent);

        var touch = event.originalEvent.changedTouches[0];
        appConsole.print(touch.clientY + "" + touch.clientX);
        searchbutton.css("top", parseInt(touch.clientY));
        searchbutton.css("left", parseInt(touch.clientX));
        searchbutton.show();
    }

    console.log($("#result"));
    $(document).on("touchend", function(event){
        showSelection(event);
    });*/
    //$(document).on("selectionchange"), function(event){
    //    console.log("selection changed");
    //}
}
$(document).ready(onDOMReady);

var app = angular.module('NorskDictApp', []);

