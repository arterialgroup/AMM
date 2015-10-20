'use strict';

angular.module('arterialeduApp')
	.controller('UserResponseController', ['$scope', '$q', 'meetingModulesHttp', 'userModuleHttp', 'userResponseHttp', 'userId', 'activityId', 'StepHttp', 'userModuleByUserId', 'QuestionsHttp', function ($scope, $q, meetingModulesHttp, userModuleHttp, userResponseHttp, userId, activityId, StepHttp, userModuleByUserId, QuestionsHttp) {
		console.log(userId);
		console.log(activityId);

		/** 
		 *  Get meeting id from meetingModule
		 *	Use meeting id to then cycle through and find all the steps for that meeting
		 *	Then use that to get all responses for that user in that meeting 
		 */

		 var stepIdArray = [];
		 $scope.stepsIdCollection = stepIdArray;

		 var savedResponseArray = [];
		 $scope.savedResponseCollection = savedResponseArray;

		 var questionArray = [];
		 $scope.questionCollection = questionArray;

		 var userModule = userModuleByUserId.getUserModule(userId);

		 userModule.then(function (response) {
		 
		 	angular.forEach(response.data, function(value){
		 		$scope.userModuleId = value.id;
		 	});
		 	
		 	// return all meetingMoudles
		 	var meeting = meetingModulesHttp.getMeetingModules();
		 	meeting.then(function (response) {			
				
				angular.forEach(response.data, function(value){				
		 			console.log('test: ',value);
		 			$scope.meetingActivityId = value.meeting.activityId;
		 
		 				if ($scope.meetingActivityId === parseInt(activityId)) {
		 					$scope.moduleId = value.module.id;				
		 				}

		 			});

		 			return $scope.moduleId;
		 
		 	}).then(function (moduleId) {

		 		var setTest = function() {
		 			var steps = StepHttp.getSteps();
		 			steps.then(function (response) {

		 				angular.forEach(response.data, function(value){			

		 					if (value.section.module.id === moduleId) {
		 						$scope.stepId = value.id;
		 						stepIdArray.push($scope.stepId);
		 					}
		 				});		 					 				

		 				angular.forEach($scope.stepsIdCollection, function(value){
		 					var savedResponse = userResponseHttp.getSavedResponse($scope.userModuleId, value);
		 					savedResponse.then(function (response) {

		 						savedResponseArray.push(response.data);

		 						var questions = QuestionsHttp.getQuestionByStep(value);
		 						questions.then(function (response) {
		 							questionArray.push(response.data);
		 							angular.forEach($scope.questionCollection, function(value, key){
		 								var value = value[0];

		 								console.log('question: ',value);

		 								$scope.savedResponseCollection[key].isGroup = value.isGroup;

		 								if (value.isGroup === false) {
		 									$scope.savedResponseCollection[key].mainQuestionText = value.questions[0].questionText;
		 								}

		 								if (value.isGroup === true) {
		 									$scope.savedResponseCollection[key].mainQuestionText = value.groupText;
		 									$scope.savedResponseCollection[key].subQuestionText = value.questions;
		 								}
		 							});
		 						});


		 					});
		 				});
		 				
		 			});
				
		 		}; // end test()
			
			
			setTest();
		 	});
		 });	




}]);

































