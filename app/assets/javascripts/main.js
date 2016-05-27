
// requirejs.onError = function(err) {
//     console.log(err);
// };

requirejs(['jquery', 'bootstrap'], function () { 
  requirejs(['app/home']); 
}); 
