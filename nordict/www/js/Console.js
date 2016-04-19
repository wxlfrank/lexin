function Console(element) {
    this.element = element;
    this.print = function(str) {
        if (this.element != null) {
            this.element.html(this.element.html() + "<br>" + str);
        }
    };
    this.reset = function(){
        this.element.html("");
    };
    this.nchar = function(n, char) {
        return char.repeat(n);
    };
    this.printObj = function(obj, tab) {
        tab = tab || 0;
        if (tab === 1)
            return;
        var str = this.nchar(tab * 4, "&nbsp;");
        if (obj == null) {
            this.print(obj);
        }
        for (var key in obj) {
            this.print(str + key + ":" + obj[key]);
            if (obj[key] != null && (typeof obj[key] === "object")) {
                this.printObj(obj[key], tab + 1);
            }
        }
    };
    this.show = function(){
      this.element.show();
    };
    this.hide = function(){
        this.element.hide(5000);
    };
    this.clear = function(){
        this.element.html("");
    };
}
var appConsole = null;

function errorHandler(message, e) {
    if (appConsole != null) {
        appConsole.print(message);
        appConsole.printObj(e);
    }
}