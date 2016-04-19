app.controller('NorskDictCtrl', function ($scope, SQLiteService) {
    $scope.word = "";
    $scope.suggestions = [];
    $scope.queryResults = [];
    $scope.trace = [];
    $scope.index = -1;

    $scope.setEditing = function(item){
        item.isEditing = !item.isEditing;
        if(item.isEditing) {
            item.tempDict = item.myDict;
        }
    };
    $scope.resetWord = function () {
        $scope.word = "";
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
    };
    $scope.back = function () {
        $scope.index--;
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
        $scope.queryResults.push($scope.trace[$scope.index]);
        $scope.word = $scope.trace[$scope.index].word;
        $scope.$apply();
    };
    $scope.forward = function () {
        $scope.index++;
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
        $scope.queryResults.push($scope.trace[$scope.index]);
        $scope.word = $scope.trace[$scope.index].word;
        $scope.$apply();
    };
    $scope.waitforEnter = function(event){
        if(event.keyCode == 13){
            $scope.queryDict();
        }
    }
    $scope.favorite = function(item){
        if(item.favorite){
            SQLiteService.deleteFavorite(item, function(){
                item.favorite = !item.favorite;
                $scope.$apply();
            });
        }else{
            SQLiteService.addFavorite(item, function(){
                item.favorite = !item.favorite;
                $scope.$apply();
            });
        }
    };
    $scope.addExplain = function(item){
        item.tempDict = item.tempDict.trim();
        if(item.tempDict.trim().localeCompare(item.myDict) != 0){
            SQLiteService.saveToMyDict(item, function(){
                item.myDict = item.tempDict;
                item.isEditing = false;
                $scope.$apply();
            });
        }
    };
    /*$scope.queryDict = function () {
     $scope.suggestions.length = 0;
     $scope.queryResults.length = 0;
     DICT.forEach(function (iter) {
     if (iter.word.startsWith($scope.word))
     $scope.queryResults.push(iter);
     });
     };
     $scope.onSelectSuggest = function (suggest) {
     $scope.suggestions.length = 0;
     $scope.queryResults.length = 0;
     $scope.word = suggest.word;
     DICT.forEach(function (iter) {
     if (iter.word.localeCompare(suggest.word) == 0 && iter.explain.localeCompare(suggest.explain) == 0) {
     $scope.queryResults.push(iter);
     }
     });
     };

     $scope.querySuggestions = function () {
     $scope.queryResults.length = 0;
     $scope.suggestions.length = 0;
     if($scope.word == null || $scope.word.length == 0){
     return;
     }
     DICT.forEach(function (iter) {
     if (iter.word.startsWith($scope.word))
     $scope.suggestions.push(iter);
     });
     if($scope.suggestions.length > 0) {
     $("#suggestions").css("height", $scope.suggestions.length * 2.02 + "em");
     }
     };*/
    /*$scope.querySelection = function(){
        $scope.word = select;
        $scope.queryDict();
        searchbutton.hide();
    }*/
    $scope.queryDict = function () {
        $scope.suggestions.length = 0;
        $scope.select = "";
        SQLiteService.query($scope.word, $scope.queryResults, function () {
            if($scope.queryResults.length > 0){
                $scope.index++;
                $scope.trace.length = $scope.index;
                $scope.trace.push($scope.queryResults[0]);
            }
            SQLiteService.queryFavorite($scope.word, $scope.queryResults, function(){
                $scope.$apply();
            });
            SQLiteService.queryMyDict($scope.word, $scope.queryResults, function(){
                $scope.$apply();
            });
        });
    };
    $scope.onSelectSuggest = function (suggest) {
        $scope.suggestions.length = 0;
        $scope.word = suggest.word;
        SQLiteService.query_exact(suggest.word, suggest.explain[0], $scope.queryResults, function () {
            if($scope.queryResults.length > 0){
                $scope.index++;
                $scope.trace.length = $scope.index;
                $scope.trace.push($scope.queryResults[0]);
            }
            SQLiteService.queryFavorite($scope.word, $scope.queryResults, function(){
                $scope.$apply();
            });
            SQLiteService.queryMyDict($scope.word, $scope.queryResults, function(){
                $scope.$apply();
            });
        });
    };

    $scope.querySuggestions = function () {
        $scope.queryResults.length = 0;
        if($scope.word == null || $scope.word.length == 0){
            return;
        }

        SQLiteService.query_suggest($scope.word, $scope.suggestions, function () {
            if($scope.suggestions.length > 0) {
                $("#suggestions").css("height", $scope.suggestions.length * 2.02 + "em");
            }
            $scope.$apply();
        });
    };
});

/*var item = new DictItem("avstand", "lengden mellom to punkter, en distanse; også om tid");
item.examples = ["på lang avstandLes høyt", "avstanden til nabohuset er tolv meterLes høyt"];
item.phrases = [
    ['vi tar avstand fra vold' , "vi misliker vold"],
    ['avstandstaking' ,  "negativ kritikk"]
];
item.composite = ["(i) gåavstand"];
item.format = "[avstanden avstander avstandene ]";
item.sound = "/a:vstan/";
item.isEditing = false;
item.favorite = false;
item.tempDict = "";
item.myDict = "";
var DICT = [
    new DictItem("a", "e1"),
    new DictItem("ah", "e1"),
    item
];*/
