'use strict';

angular.module('arterialeduApp')
.controller('MyModulesDetailController', ['$rootScope', '$scope', '$state', '$stateParams', 'ServiceIdService', 'MyLearningSection', 'MyLearningSectionHttp', 'Step', 'StepHttp', 'UserModule', 'Account', 'AccountHttp', 'UserHttp', 'UserProgressHttp', 'userModuleHttp', 'moduleId', function ($rootScope, $scope, $state, $stateParams, ServiceIdService, MyLearningSection, MyLearningSectionHttp, Step, StepHttp, UserModule, Account, AccountHttp, UserHttp, UserProgressHttp, userModuleHttp, moduleId){

	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	/*	----------------------------------------------------------------------------------------------------
	 *	get section by module */
	$scope.sections = MyLearningSection.query({moduleId: moduleId});

	// var testSections = MyLearningSectionHttp.getSectionByModuleId(moduleId);
	// testSections.then(function (data) {
	//

	// 	angular.forEach(data, function(value, key){
			
	// 	});

	// }), function(err) {
	//
	// };

	var startSection = [];
	$scope.startSection = startSection;

	/*	----------------------------------------------------------------------------------------------------
	 *	get steps in section */
	var stepsArray = [];
	$scope.stepsArray = stepsArray;
	// return sections by moodule - http
	var sectionByModule = MyLearningSectionHttp.getSectionByModuleId(moduleId);
	sectionByModule.then(function (data) {
		

		angular.forEach(data.data, function(value, key){
			

			var stepsInSection = StepHttp.getStepBySection(value.id);

			stepsInSection.then(function (data) {
				

				// get each id of step 
				angular.forEach(data.data, function(value, key){
					
					stepsArray.push(value.id)

				});
				
			})
		});
	})

	/*	----------------------------------------------------------------------------------------------------
	 *	get id for section within the module */

	// array to push new collection to
	var sectionIdCollection = [];
	$scope.sectionIdCollection = sectionIdCollection;

	// return section by module
	sectionByModule.then(function(data) {
		
		// setting setSectionArray - with a list of section ids that a link to the module
		angular.forEach(data.data, function(value){
			// pushing id of return sections to a new collection
			sectionIdCollection.push(value.id);
			ServiceIdService.setSectionsArray = $scope.sectionIdCollection;		
		});
		
		
		// setting first section witin the module in factory
		function setFirstStep() {		
			
			ServiceIdService.setId = $scope.sectionIdCollection[0];
			
			
			
			// padding first section id to fetch section steps
			getStepsFunc($scope.sectionIdCollection[0]);

		}
		setFirstStep();
	});


	/*	----------------------------------------------------------------------------------------------------
	 *	START step logic
	 *	return first steps for startActivity 
	 *	if there is a start but not step logged gets first step */

	var steps = [];
	$scope.steps = steps;

	// getting steps for first section
	function getStepsFunc(sectionId) {

		// get userProgress and if step id is null do below and if not create continue
		accounthttp.then(function (accountObj) {
			
			$scope.accountId = accountObj.data.id;
		
			var userMoudleIdCollection = [];
			$scope.userMoudleIdCollection = userMoudleIdCollection;

			var userModuleObj = userModuleHttp.userByUserId($scope.accountId);
			userModuleObj.then(function (data) {
			
				angular.forEach(data.data, function(value, key){
					userMoudleIdCollection.push(value.id);
				});

				angular.forEach($scope.userMoudleIdCollection, function(value, key){
					
					
					// if it cant fin prgress so first step otherwise do continue
					var userProgressObj = UserProgressHttp.getProgress(value);
					
					// userProgress obj
					userProgressObj.then(function (data) {
				
						$scope.userProgressData = data.data.stepId;
						var endDate = data.data.endDate;
						
						
						
						$scope.startActivity = $scope.userProgressData;
						
							var currentStep = $scope.startActivity;
							var idx = $scope.stepsArray.indexOf(currentStep);


							if(idx > -1 && idx < ($scope.stepsArray.length-1)) {
								
								var newId = $scope.stepsArray[idx+1];
								$scope.startActivity = newId;
								$scope.submitStage = false;
								
								
								startSection.push(
								{
									newId: newId
								}
								);

								angular.forEach($scope.startSection, function (value, key) {
									
									$scope.sections[key].newId = value;

								});


							} else if (idx != -1 && idx >= ($scope.stepsArray.length-1) && endDate !== null) {
						
								$scope.completion = true;
								$scope.submitStage = false;
						
								var newId = $scope.stepsArray[idx+1];
								$scope.startActivity = newId;
								
								startSection.push(
								{
									newId: newId
								}
								);
								angular.forEach($scope.startSection, function (value, key) {
									
									$scope.sections[key].newId = value;

								});

							} else if (idx != -1 && idx >= ($scope.stepsArray.length-1) && endDate == null) {
						
								$scope.submitStage = true;
								$scope.completion = false;
								var newId = $scope.stepsArray[idx];
								$scope.startActivity = newId;

								startSection.push(
								{
									newId: newId
								}
								);
								angular.forEach($scope.startSection, function (value, key) {
									
									$scope.sections[key].newId = value;

								});

								$scope.goToSubmit = function(sectionId) {
									$state.go('confirm', { id : sectionId });
								};

								//$state.go('confirm', { id : $scope.sectionId });

							} else if (idx == -1) {
								
								$scope.submitStage = false;
								var stepsBySectionId = StepHttp.getStepBySection(sectionId);
								stepsBySectionId.then(function(data) {

									var stepsObjs = data.data;

									angular.forEach(stepsObjs, function(value){

										steps.push(value.id);
									});

									var startId = $scope.steps[0];

									startSection.push({
										newId: startId
									});

									angular.forEach($scope.startSection, function (value, key) {
										
										

									// Take value and match it to the array position 
									var positionOf = {
										testId: key
									}
									
									$scope.sections[key].testId = positionOf;
									$scope.sections[key].newId = value;

									});
								});
							}

						}, function (err) {
							
							// no progress found, so create :id for sections by retrieving and 
							// adding first step to newId on section obj
							$scope.submitStage = false;
							var stepsBySectionId = StepHttp.getStepBySection(sectionId);

							// get steps by section
							stepsBySectionId.then(function(data) {
								var stepsObjs = data.data;



								// store step id
								angular.forEach(stepsObjs, function(value){
									steps.push(value.id);
								});

								

								// store first step id in startId from steps arry
								var startId = $scope.steps[0];

								
								startSection.push({
									newId: startId
								});

								// add the newId to $scope.section to use in the ng-repeat
								angular.forEach($scope.startSection, function (value, key) {

									
									// Take value and match it to the array position 
									var positionOf = {
										testId: key
									}
									
									$scope.sections[key].testId = positionOf;
									$scope.sections[key].newId = value;
								}); //$scope.sections.newId = value;

							}); // end of stepsBySectionId
						}); // end of userProgressObj
				}); // end forEach - userMoudleIdCollection
			}); // end userModuleObj
		}); // end accounthttp
	} // end getStepsFunc

	/*	----------------------------------------------------------------------------------------------------
	 *	END step logic */


	// get userProgress step to continue if activity started
	accounthttp.then(function (accountObj) {
		$scope.accountId = accountObj.data.id;

		// useModule collection
		var userMoudleIdCollection = [];
		var userModuleObj = userModuleHttp.userByUserId($scope.accountId);

		userModuleObj.then(function (data) {
		
			angular.forEach(data.data, function(value, key){
				userMoudleIdCollection.push(value.id);
				
			});
			// now all userModule id for this module are in a new array
			// use them to get progress

			angular.forEach(userMoudleIdCollection, function(value, key){

				var userProgressObj = UserProgressHttp.getProgress(value);
				userProgressObj.then(function (data) {
					
					//return data;
				});
				// now we have access to each stepId we can use those for the continue button	
			});	
		});
	});
	
 
	/*	----------------------------------------------------------------------------------------------------
	 *	userProgress functionality */

	// get current user account 
	$scope.account = Account.get();
	var data = $scope.account;

	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	// create and format date
	var createDate = new Date();
	var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();

	// createUserModule is commentted out becausae the plan is to create this when we put in there name with the upload for this first phase 
	// but then this is needed for the next phasae
	$scope.activityModule = {
		createUserModule: function() {
			/* THIS TO STAY FOR FUTURE USE */
			// var userLogin = data.login;

			// $scope.user = UserHttp.getCurrentUser(userLogin);
			// var userData = $scope.user;

			// userData.then(function(data) {

			// 	function create() {
			// 		UserModule.update(
			// 		{
			// 			"id": 0,
			// 			"module": {
			// 				"id": 1

			// 			},
			// 			"user": {
			// 				"id": data.data.id,

			// 				"activated": data.data.activated
			// 			}
			// 		}
			// 		);
			// 	}

			// 	return create();
			// });	
			this.startProgress();
		},
		startProgress: function() {

			var getUserId = function() {
				accounthttp.then(function(data) {
					getUserModuleById(data.data.id);
				});

			};
			var getUserModuleById = function(data) {
				var returnUserModuleById = userModuleHttp.userByUserId(data);
				returnUserModuleById.then(function(data) {
					return data.data;
				}).then(function(data) {

					angular.forEach(data, function(value){
						
						startUserProgress(value.id);
						return value.id;
					});
				});

				var startUserProgress = function(id) {
					function start() {
						UserProgressHttp.startProgress({
							"userProgressId": 0,
							"userModuleId": id,
							"stepId": "",
							"startDate": formatDate,
							"endDate": ""
						});
					}
					return start();
				};
			};
			getUserId();
		}
	}

}]).directive('headingDirecitve', function() {
	// Heading directive for Module title : location - partials/module-heading.html
	return {
		restrict: 'E',
		replace: true,
		templateUrl: 'scripts/app/myLearning/partials/module-heading.html'
	};
});
