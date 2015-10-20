'use strict';

angular.module('arterialeduApp')
    // .controller('MyModulesController', ['$scope', 'Module', function ($scope, Module) {
    .controller('MyModulesController', ['$scope', 'Module', 'MyLearningSteps', 'UserModule', 'userModuleByUserId', 'userModuleHttp', 'AccountHttp', 'AttendeesHttp', 'MyLearningSectionHttp', 'UserProgressHttp', 'StepHttp', 'UserHttp', function ($scope, Module, MyLearningSteps, UserModule, userModuleByUserId, userModuleHttp, AccountHttp, AttendeesHttp, MyLearningSectionHttp, UserProgressHttp, StepHttp, UserHttp){
       // $scope.modules = Module.query();

       	//mailService.sendEmail("myemail@gmail.com", "subject hi", "hi",false, true);
       

        $scope.acountHttp = AccountHttp.getAccount();
		var accounthttp = $scope.acountHttp;

		var test = [];
		$scope.test = test;
		

		var modulesForMeeting = [];
		$scope.modulesForMeeting = modulesForMeeting;

		// $scope.attendees = AttendeesHttp.getAttendeesById()
		// 

		accounthttp.then(function(accountInfo) {
			var accountCreds = accountInfo.data
			//	
			return accountCreds;

		}).then(function (accountCreds){
			var meeting = AttendeesHttp.getAttendeesById(accountCreds.id);
			meeting.then(function (data) {
				//
				$scope.meeting = data.data.meeting;
			})

		});
	
		accounthttp.then(function(accountInfo) {
			var accountCreds = accountInfo.data
			
			return accountCreds;

		}).then(function (accountCreds){
			var userModule = userModuleByUserId.getUserModule(accountCreds.id);
			//
			return userModule;

		}).then(function (userModule) {
			angular.forEach(userModule.data, function(value){
				modulesForMeeting.push(value.module)

				//
				//
			});
			//

			angular.forEach($scope.modulesForMeeting, function(moduleObj, key){
				

				var section = MyLearningSectionHttp.getSectionByModuleId(moduleObj.id);

				

				section.then(function (data) {
					//

					var stepsArray = [];
					$scope.stepsArray = stepsArray;

					angular.forEach(data.data, function(value, key){
						//

						var steps = StepHttp.getStepBySection(value.id);

						steps.then(function (data) {
							//
							var stepsObjArray = data.data;

							angular.forEach(stepsObjArray, function(value, key){
								//
								stepsArray.push(value.id);
							});

							//

							// check where the progress is up to
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

										//
										var userProgressObj = UserProgressHttp.getProgress(value);

										userProgressObj.then(function (data) {


											$scope.userProgressData = data.data.stepId;

											$scope.currentStep = $scope.userProgressData;


											var currentStep = $scope.currentStep;
											var idx = $scope.stepsArray.indexOf(currentStep);

											//
											//


										
										
											// This needs to check if there is a dataEnd
											var endDate = data.data.endDate;
											
											// if (idx != -1 && idx >= ($scope.stepsArray.length-1)) {
											if (endDate) {
												
												// $scope.completion = true;
												test.push(
													{
														status: true,
													}
												);

											} else {
												
												//$scope.completion = false;
												test.push(
													{
														status: false
													}
												);
												
											}
											

											angular.forEach($scope.test, function (value, key) {
												//


												$scope.modulesForMeeting[key].completed = value;

											});

										});

									}, function(err) {
										
										
									});
							});
							});
							});



							});

							});
				// end of section promise


			});
		});


}]);
