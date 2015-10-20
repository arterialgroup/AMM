'use strict';

angular.module('arterialeduApp')
.controller('ModuleSectionDetailController', ['$scope', 'MyLearningSteps', 'UserModule', 'sectionId', function ($scope, MyLearningSteps, UserModule, sectionId){
	$scope.steps = MyLearningSteps.query({sectionId: sectionId});
	$scope.userModule = UserModule.query();
}]);