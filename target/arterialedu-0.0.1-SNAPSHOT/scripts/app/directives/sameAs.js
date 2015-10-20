angular.module('arterialeduApp')
.directive('sameAs', function () {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ctrl) {
        var modelToMatch = attrs.sameAs.val;

        console.log('modelToMatch: ',modelToMatch);      
        
        scope.$watch(attrs.sameAs, function() {
          ctrl.$validate();
          console.log('ctrl: ',ctrl);
          console.log('attrs: ',attrs);
        })
        
        ctrl.$validators.match = function(modelValue, viewValue) {
          return viewValue === scope.$eval(modelToMatch);
        };
    }
    };
});