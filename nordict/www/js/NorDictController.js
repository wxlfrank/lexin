app.controller('NorskDictCtrl', function ($scope, SQLiteService) {
    $scope.word = "";
    $scope.suggestions = [];
    $scope.queryResults = [];
    $scope.queryDict = function () {
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
        SQLiteService.query($scope.word, function (res) {
            for (var i = 0; i < res.rows.length; i++) {
                var row = res.rows.item(i);
                $scope.queryResults.push(getDictItemFromRecord(row));
            }
            $scope.$apply();
        });
    };
    $scope.resetWord = function () {
        $scope.word = "";
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
    };
    $scope.onSelectSuggest = function (suggest) {
        $scope.suggestions.length = 0;
        $scope.queryResults.length = 0;
        $scope.word = suggest.word;
        SQLiteService.query_exact(suggest.word, suggest.explain, function (res) {
            for (var i = 0; i < res.rows.length; i++) {
                var row = res.rows.item(i);
                $scope.queryResults.push(getDictItemFromRecord(row));
            }
            $scope.$apply();
        });
    };

    $scope.querySuggestions = function () {
        $scope.queryResults.length = 0;
        $scope.suggestions.length = 0;
        if($scope.word == null || $scope.word.length == 0){
            return;
        }

        SQLiteService.query_suggest($scope.word, function (res) {
            for (var i = 0; i < res.rows.length; i++) {
                var row = res.rows.item(i);
                $scope.suggestions.push(new DictItem(row["word"], row["explain"]));
            }
            if($scope.suggestions.length > 0) {
                $("#suggestions").css("height", $scope.suggestions.length * 2.02 + "em");
            }
            $scope.$apply();
        });
    };
});