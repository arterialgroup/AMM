'use strict';

angular.module('arterialeduApp')
    // .controller('MyModulesController', ['$scope', 'Module', function ($scope, Module) {
    .controller('MyModulesController', ['$scope', 'Module', 'MyLearningSteps', 'UserModule', 'userModuleByUserId', 'userModuleHttp', 'AccountHttp', 'AttendeesHttp', 'MyLearningSectionHttp', 'UserProgressHttp', 'StepHttp', 'UserHttp', function ($scope, Module, MyLearningSteps, UserModule, userModuleByUserId, userModuleHttp, AccountHttp, AttendeesHttp, MyLearningSectionHttp, UserProgressHttp, StepHttp, UserHttp){
       // $scope.modules = Module.query();

       	//mailService.sendEmail("myemail@gmail.com", "subject hi", "hi",false, true);
       console.log('mailservice: ');

        $scope.acountHttp = AccountHttp.getAccount();
		var accounthttp = $scope.acountHttp;

		var test = [];
		$scope.test = test;
		

		var modulesForMeeting = [];
		$scope.modulesForMeeting = modulesForMeeting;

		// $scope.attendees = AttendeesHttp.getAttendeesById()
		// console.log('$scope.attendees: ',$scope.attendees);

		accounthttp.then(function(accountInfo) {
			var accountCreds = accountInfo.data
			//	console.log('accountCreds: ',accountCreds);
			return accountCreds;

		}).then(function (accountCreds){
			var meeting = AttendeesHttp.getAttendeesById(accountCreds.id);
			meeting.then(function (data) {
				//console.log('data: ',data.data.meeting);
				$scope.meeting = data.data.meeting;
			})

		});
	
		accounthttp.then(function(accountInfo) {
			var accountCreds = accountInfo.data
			console.log('accountCreds: ',accountCreds);
			return accountCreds;

		}).then(function (accountCreds){
			var userModule = userModuleByUserId.getUserModule(accountCreds.id);
			//console.log('userModle: ',userModule);
			return userModule;

		}).then(function (userModule) {
			angular.forEach(userModule.data, function(value){
				modulesForMeeting.push(value.module)

				//console.log('value: ',value);
				//console.log('value.module: ',value.module);
			});
			//console.log('modules: ',$scope.modulesForMeeting);

			angular.forEach($scope.modulesForMeeting, function(moduleObj, key){
				

				var section = MyLearningSectionHttp.getSectionByModuleId(moduleObj.id);

				

				section.then(function (data) {
					//console.log('section: ',data.data);

					var stepsArray = [];
					$scope.stepsArray = stepsArray;

					angular.forEach(data.data, function(value, key){
						//console.log('value: ',value);

						var steps = StepHttp.getStepBySection(value.id);

						steps.then(function (data) {
							//console.log('steps: ',data.data);
							var stepsObjArray = data.data;

							angular.forEach(stepsObjArray, function(value, key){
								//console.log('value: ',value.id);
								stepsArray.push(value.id);
							});

							//console.log('$scope.stepsArray: ',$scope.stepsArray);

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

										//console.log('just before getting progress');
										var userProgressObj = UserProgressHttp.getProgress(value);

										userProgressObj.then(function (data) {


											$scope.userProgressData = data.data.stepId;

											$scope.currentStep = $scope.userProgressData;


											var currentStep = $scope.currentStep;
											var idx = $scope.stepsArray.indexOf(currentStep);

											//console.log('currentStep: ',currentStep);
											//console.log('idx: ',idx);


										
										
											// This needs to check if there is a dataEnd
												var endDate = data.data.endDate;

											// if (idx != -1 && idx >= ($scope.stepsArray.length-1)) {
											if (endDate) {
												console.log('user is complete');
												// $scope.completion = true;
												test.push(
													{
														status: true,
													}
												);

											} else {
												console.log('user is not complete');
												//$scope.completion = false;
												test.push(
													{
														status: false
													}
												);
												
											}
											console.log('$scope.test: ',$scope.test.reverse());

											angular.forEach($scope.test, function (value, key) {
												//console.log('value in test: ',value);
												$scope.modulesForMeeting[key].completed = value;

											});

										});

									}, function(err) {
										console.log(err);
										console.log('not progress found create start');
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
