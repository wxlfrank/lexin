var fileService = function () {
    var log = function (message) {
        if (appConsole != null)
            appConsole.print(message);
    }
    this.resolveURL = function (dirURL, success, failure) {
        failure = failure
            || errorHandler.bind(null,
                "Error happens when resolving url " + dirURL);
        success = success || function () {
                log("Resolve " + dirURL + " successfully!");
            };
        window.resolveLocalFileSystemURL(dirURL, success, failure);
    };

    this._getFile = function (dirEntry, fileName, success, failure) {
        var fullFileName = dirEntry.toURL() + fileName;
        log("check whether " + fullFileName + " exists!");
        success = success || function () {
                log(fullFileName + " exists!");
            };
        failure = failure
            || errorHandler.bind(null,
                "Error happens when finding file " + fullFileName);
        dirEntry.getFile(fileName, {}, success, failure);
    };
    this.getFile = function (dirURL, fileName, success, failure) {
        var resolveSuccess = function (dirEntry) {
            this._getFile(dirEntry, fileName, success, failure);
        };
        this.resolveURL(dirURL, resolveSuccess, failure);
    };

    /*this._getDir = function(dirEntry, subDirName, success, failure) {
     var fullDirName = dirEntry.toURL() + subDirName;
     log("check whether " + fullDirName + " exists!");
     success = success || function() {
     };
     failure = failure
     || errorHandler.bind(null,
     "Error happens when finding directory "
     + fullDirName);
     dirEntry.getFile(subDirName, {}, success, failure);
     };
     this.getDir = function(dirURL, subDirName, success, failure) {
     var resolveSuccess = function(dirEntry) {
     this._getDir(dirEntry, subDirName, success, failure);
     }
     this.resolveURL(dirURL, resolveSuccess, failure);
     };*/

    /*var createDirectFile = function(dirEntry, fileName, success, failure) {
     var fullFileName = dirEntry.toURL() + fileName;
     log(fullFileName + " creation");
     success = success || function() { };
     var findFileFailure = function(error) {
     failure = failure || errorHandler.bind(null, "Error happens when creating " + fullFileName);
     dirEntry.getFile(fileName, { create : true, exclusive : true }, success, failure);
     };
     dirEntry.getFile(fileName, {}, success, findFileFailure);
     };*/
    /*var createDirectDir = function(dirEntry, dirName, success, failure) {
     var fullDirName = dirEntry.toURL() + dirName;
     success = success || function() { };
     var findFileFailure = function(error) {
     log("Cannot find " + fullDirName);
     failure = failure || errorHandler.bind(null, "Error happens when creating " + fullDirName);
     dirEntry.getDirectory(dirName, { create : true, exclusive : true }, success, failure);
     };
     dirEntry.getDirectory(dirName, {}, success, findFileFailure);
     };

     var createDirEx = function(dirEntry, dirArray, index, success, failure) {
     var dir = dirArray[index];
     if (index === dirArray.length - 1){
     createDirectDir(dirEntry, dir, success, failure);
     }
     else {
     var createDirSuccess = function(nextDirEntry) {
     createDirEx(nextDirEntry, dirArray, index + 1, success, failure);
     };
     log(index);
     createDirectDir(dirEntry, dir, createDirSuccess, failure);
     }
     };
     this._createDir = function(dirEntry, path, success, failure) {
     if (path.startsWith("/"))
     path = path.substr(1);
     if (path.endsWith("/"))
     path = path.substr(0, path.length - 1);
     var dirArray = path.split("/");
     if(dirArray.length > 0)
     createDirEx(dirEntry, dirArray, 0, success, failure);
     };*/
    /*this._createFile = function(dirEntry, path, success, failure) {
     dirEntry.getFile(path, {create:true, exclusive: true}, success, failure);
     return;
     if (path.startsWith("/"))
     path = path.substr(1);
     if (path.endsWith("/"))
     path = path.substr(0, path.length - 1);
     var dirArray = path.split("/");
     if(dirArray.length < 1) return;
     var fileName = dirArray[dirArray.length - 1];
     if(dirArray.length > 1){
     dirArray= dirArray.splice(dirArray.length - 2, 1);
     log(dirArray);
     var dirCreateSuccess = function(finalDirEntry) {
     log("directory creation succeed!");
     createDirectFile(finalDirEntry, fileName, success, failure);
     };
     createDirEx(dirEntry, dirArray, 0, dirCreateSuccess, failure);
     }else
     createDirectFile(finalDirEntry, fileName, success, failure);
     };*/
    /*this.createFile = function(dirURL, path, success, failure) {
     failure = failure || errorHandler.bind(null, "Error happens when creating " + dirURL + path);
     var resolveSuccess = function(dirEntry) {
     this._createFile(dirEntry, path, success, failure);
     };
     this.resolveURL(dirURL, resolveSuccess, failure);
     };*/

    /*this.createDir = function(dirURL, path, success, failure) {
     var resolveSuccess = function(dirEntry) {
     this._createDir(dirEntry, path, success, failure);
     };
     this.resolveURL(dirURL, resolveSuccess, failure);
     };*/

    /*this.getBinary = function(fileEntry, success, failure) {
     log("begin to read " + fileEntry.toURL());
     failure = failure || errorHandler.bind(null, "Error happens when reading " + fileEntry.toURL());
     var fileReadSuccess = function(e) {
     log("read " + fileEntry.toURL() + " successfully!");
     if (success != null) {
     success(new Blob([ this.result ]));
     }
     };
     var getFileSuccess = function(file) {
     log("get File " + fileEntry.toURL() + " successfully!");
     var reader = new FileReader();
     reader.onloadend = fileReadSuccess;
     reader.onerror = failure;
     reader.readAsArrayBuffer(file);
     };
     fileEntry.file(getFileSuccess, failure);
     };*/
    /*this.writeBinary = function(fileEntry, blob, success, failure) {
     failure = failure || errorHandler.bind(null, "Error happens when writing " + fileEntry.toURL());
     fileEntry.createWriter(function(fileWriter) {
     success = success || function(){
     appConsole.print("Write file " + fileEntry.toURL() + " successfully!");
     }
     fileWriter.onwriteend = success;
     fileWriter.onerror = failure;
     fileWriter.write(blob);
     }, failure);
     };*/
    return this;
}();
/*DBCopyService = {
 copyDB : function(dbName, success) {
 dbName = dbName || "dict.db";
 var targetDir = cordova.file.applicationStorageDirectory;
 var target = "databases/" + dbName;
 var sourceExist = function(sourceFile) {
 var targetFileCreationSuccess = function(targetFile) {
 appConsole.print("create " + target + " successfully!");
 fileService.getBinary(sourceFile, function(blob) {
 fileService.writeBinary(targetFile, blob, success);
 });
 };
 fileService.createFile(targetDir, target, targetFileCreationSuccess);
 };
 var targetNotExist = function(error) {
 var sourceDir = cordova.file.applicationDirectory + "www/res/database/";
 fileService.getFile(sourceDir, dbName, sourceExist);
 };
 fileService.getFile(targetDir, target, null, targetNotExist);
 }
 };*/

function downloadDB() {
    var sqliteDir = cordova.file.applicationStorageDirectory;
    var databasePath = "databases/dict.db";
    fileService.getFile(sqliteDir, databasePath, function(entry){
        initDB();
    }, function dirNotExist(error) {
        if (appConsole == null) {
            appConsole = new Console($("body>.appConsole:first"));
        }
        appConsole.element.show();
        var fileTransfer = new FileTransfer();
        var uri = encodeURI("https://docs.google.com/uc?id=0B3UhvZT3c8xNUmhiQTYwOFZyWUE&export=download");
        fileTransfer.download(
            uri,
            sqliteDir + databasePath,
            function (entry) {
                appConsole.print("Database download successfully to " + entry.toURL());
                appConsole.element.hide(5000);
                initDB();
            },
            function (error) {
                appConsole.print("Error happens when downloading database from " + error.source + " to " + error.target);
            },
            false,
            {}
        );
    });
}