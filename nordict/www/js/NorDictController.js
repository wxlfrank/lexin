app.controller('NorskDictCtrl', function ($scope, SQLiteService) {
    $scope.word = "";
    $scope.suggestions = [];
    $scope.queryResults = [];
    $scope.resetWord = function () {
        $scope.word = "";
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
    };
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
    $scope.queryDict = function () {
        $scope.suggestions.length = 0;
        SQLiteService.query($scope.word, $scope.queryResults, function () {
            SQLiteService.queryFavorite($scope.word, $scope.queryResults, function(){
                $scope.$apply();
            });
        });
    };
    $scope.onSelectSuggest = function (suggest) {
        $scope.suggestions.length = 0;
        $scope.word = suggest.word;
        SQLiteService.query_exact(suggest.word, suggest.explain, $scope.queryResults, function (res) {
            SQLiteService.queryFavorite($scope.word, $scope.queryResults, function(){
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

/*
 var item = new DictItem("avstand", "lengden mellom to punkter, en distanse; også om tid");
 item.examples = ["på lang avstandLes høyt", "avstanden til nabohuset er tolv meterLes høyt"];
 item.phrases = [
 ['vi tar avstand fra vold' , "vi misliker vold"],
 ['avstandstaking' ,  "negativ kritikk"]
 ];
 item.composite = ["(i) gåavstand"];
 item.format = "[avstanden avstander avstandene ]";
 item.sound = "/a:vstan/";
 var DICT = [
 new DictItem("a", "e1"),
 new DictItem("ah", "e1"),
 item
 ];*/
