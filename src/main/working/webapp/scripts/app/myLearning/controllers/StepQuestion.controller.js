'use strict';

angular.module('arterialeduApp')
	.controller('StepQuestion.controller', ['$scpoe' ,'Question', 'stepId', function($scope, Question, stepId) {
		$scope.questions = Question.query();
}]);