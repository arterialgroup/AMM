// 'use strict';

angular.module('arterialeduApp')
.controller('ConfirmController', ['$rootScope', '$scope', '$state', 'userResponseHttp', '$controller', 'SectionsHttp', 'Step', 'StepHttp', 'QuestionByStep', 'QuestionsHttp', 'UserProgressHttp', 'sectionId', 'UserHttp', 'AccountHttp', 'userModuleHttp', '$stateParams', 'ServiceIdService', function ($rootScope, $scope, $state, userResponseHttp, $controller, SectionsHttp, Step, StepHttp, QuestionByStep, QuestionsHttp, UserProgressHttp, sectionId, UserHttp, AccountHttp, userModuleHttp, $stateParams, ServiceIdService) {

	/*	----------------------------------------------------------------------------------------------------
	 *	getting section obj - creating a stepsNavArray */
	
	// scope and array
	var stepsNavArray = [];
	$scope.stepsNavArray = stepsNavArray;

	// call to http
	SectionsHttp.getSectionsById(sectionId).then(function (data) {
		return data.data;
	}).then(function (section) {
		StepHttp.getStepBySection(section.id).then(function (steps) {

			angular.forEach(steps.data, function(value, key){
				stepsNavArray.push(value.id);
			});
		}), function (err) {

		};

	}), function (err) {
		
	};
	
	/*	----------------------------------------------------------------------------------------------------
	 *	getting userModule */

	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	function getUserModule() {
		accounthttp.then(function(data) {
			getUserModuleById(data.data.id);
		});

		function getUserModuleById(data) {
			
			var returnUserModuleById = userModuleHttp.userByUserId(data);

			returnUserModuleById.then(function(data) {
				
				return data.data;

			}).then(function(data) {


				var userModuleIdSeperateArray = [];
				var userModuleIdSeperate = userModuleIdSeperateArray;

				for(var prop in data) {
					
					var moduleID = $stateParams.sectionId;

					angular.forEach(data, function(value, key){


						var valueModuleId = value.module.id;
						var stateModuleId = parseInt($stateParams.id);
						
						if (valueModuleId == stateModuleId) {				
							
							userModuleIdSeperate.push({
								id: value.id	
							});
						}

					});

					angular.forEach(userModuleIdSeperate, function(value, key){
						var id = value.id;
						getUpdateUserProgress(id);
						$scope.testResponse = id;
					});
				}
			});

			function getUpdateUserProgress(id) {
				
				var UserProgressReturned = UserProgressHttp.getProgress(id);
				

				UserProgressReturned.then(function(data) {
					
					var userModelIdGlobal = data.data.userModuleId;
					$scope.userModelIdGlobal = userModelIdGlobal;
					
					
					//
				});
			};
		}
	}
	getUserModule();

	/*	----------------------------------------------------------------------------------------------------
	*	prev page  */

	$scope.prevPage = function() {
		
		var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
		var idx = lastArrayItem;

		if(idx != -1) {

			var newId = stepsNavArray[idx-1];
			
			$state.go('sectionStepDetail', { sectionId : sectionId, id : newId });
		}
	}

	/*	----------------------------------------------------------------------------------------------------
	 *	Progress tracking  */

	var createDate = new Date();
	var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();

	$scope.submitted = false;
	$scope.confirm = {};

	$scope.confirmSection = function(answer) {

		
		if($scope.confirmForm.$valid) {
			var updateProgress = function(id) {

			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
			var UserProgressReturned = UserProgressHttp.getProgress(id);
			
			UserProgressReturned.then(function (data) {
				
				return data;

			}).then(function (data) {
				

				// need to get module info
				
				var lastStep = $scope.stepsNavArray[$scope.stepsNavArray.length - 1];
				UserProgressHttp.endProgress(
				{
					"userProgressId": data.data.userProgressId,
					"userModuleId": $scope.userModelIdGlobal, //data.data.userModuleId,
					"stepId": lastStep,
					"startDate": "",
					"endDate": formatDate
				}
				);

			}), function (err) {
	
			}
		}

		var nextPage = function() {
			$state.go('myModules');
		}

		
		updateProgress($scope.userModelIdGlobal);
		nextPage();
		
		} else {
			$scope.confirmForm.submitted = true;
		}
	}

}]);











