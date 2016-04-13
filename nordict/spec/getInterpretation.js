/**
 * Created by wxlfr_000 on 2016/2/8.
 */
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
    }
    expect(getTable("jeg").localeCompare("j")).toEqual(0);
    var a = ["a"];
    f(a);
    console.log(a);
  });
});
