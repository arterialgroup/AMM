angular.module('arterialeduApp')
.directive('nextprevUi', function() {
	 return {
      restrict: 'E',
      require: 'SectionStepsDetailController',
      replace: true,
      templateUrl: 'scripts/app/myLearning/partials/nextprev-ui.html'
  };
});