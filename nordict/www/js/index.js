function onDeviceReady() {
    appConsole.print("Device is ready!");
    downloadDB();
    appConsole.print("Download database!")
}
document.addEventListener("deviceReady", onDeviceReady, false);
function onDOMReady() {
    appConsole = new Console($("body>.appConsole:first"));
}
$(document).ready(onDOMReady);

var app = angular.module('NorskDictApp', []);

