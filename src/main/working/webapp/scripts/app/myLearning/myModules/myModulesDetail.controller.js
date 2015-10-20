'use strict';

angular.module('arterialeduApp')
	.controller('MyModulesDetailController', ['$scope', 'MyLearningSection', 'MyLearningSteps', 'StepsHttp', 'SectionHttp', 'moduleId', function ($scope, MyLearningSection, MyLearningSteps, StepsHttp, SectionHttp, moduleId){

		$scope.sections = MyLearningSection.query({moduleId: moduleId});

}]).directive('headingDirecitve', function() {
	return {
		restrict: 'E',
		replace: true,
		templateUrl: 'scripts/app/myLearning/myModules/partials/module-heading.html'
	};
});
