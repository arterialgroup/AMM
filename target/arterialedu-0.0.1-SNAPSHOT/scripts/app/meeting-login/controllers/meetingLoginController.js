'use strict';

angular.module('arterialeduApp')
.controller('MeetingLoginController', ['$rootScope', '$state', '$scope', 'registerUser', 'MeetingHttp', 'Auth', 'AccountHttp', function ($rootScope, $state, $scope, registerUser, MeetingHttp, Auth, AccountHttp) {
	
	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	accounthttp.then(function (data) {
		
		if (data.data) {
			
			$state.go('myModules');
		};

	}, function (err) {
		
	});

	$scope.submitted = false;
	$scope.activityId = '27893';
	$scope.loginValidation = {
		'submitted': false,
		'validation': false,
		'allRequired': false,
		'wrongActivity': false,
		'accreditation': false //RACGP
	}

	$scope.stateValues = [
		{
			id: 1,
			state: 'NSW',
		}, 
		{
			id: 2,
			state: 'VIC',			
		},
		{
			id: 3,
			state: 'ACT',			
		},
		{
			id: 4,
			state: 'QLD',			
		},
		{
			id: 5,
			state: 'SA',			
		},
		{
			id: 6,
			state: 'WA',			
		},
		{
			id: 7,
			state: 'NT',			
		},
		{
			id: 8,
			state: 'TAS',			
		}
	];

	$scope.accValues = [
		{
			id: 1,
			acc: 'ACRRM',
		}, 
		{
			id: 2,
			acc: 'RACGP',			
		},
	];

	$scope.selected = { name: 'aSubItem' };

	// login function - We are checking if the form is logged in
	// then using validation and then if all ok logging in
	$scope.login = function(userInfo) {

		

		if ($scope.userForm.$valid) {

			var acctype = userInfo.accType.acc;
			if(userInfo.meetingId == '27893') {
				

				if (userInfo.email === userInfo.emailConfirm) {
					if (userInfo.accType.id === 1 && userInfo.password.length < 7 || userInfo.password.length == null) {

						$scope.loginValidation = {
							'validation': true,
							'allRequired': false,
							'wrongActivity': false,
							'accreditationACRRM': true, //accreditation
						}

					} else if (userInfo.accType.id === 2 && userInfo.password.length < 6 || userInfo.password.length == null) {
						$scope.loginValidation = {
							'validation': true,
							'allRequired': false,
							'wrongActivity': false,
							'accreditationRACGP': true, //accreditation
						}
					} else {


						registerUser.register({
							"meetingId": userInfo.meetingId,
							"login": userInfo.email,
							"password": userInfo.password,
							"accreditation": userInfo.password,
							"accreditationType": acctype,
							"firstName": userInfo.fname,
							"lastName": userInfo.lname,
							"email": userInfo.email,
							"state": userInfo.state.state,
							"langKey": "en",
							"roles": [
							""
							]

						}).then(function (credentials) {

							Auth.login({
								username: credentials.email,
								password: credentials.password,
								//rememberMe: $scope.rememberMe

							}).then(function () {

								$scope.authenticationError = false;

								if ($rootScope.previousStateName === '/') {
									$state.go('myModules');
								} else {
						
									$state.go('myModules');
								}
							}).catch(function () {
								$scope.authenticationError = true;
								
								$scope.loginValidation = {
									'validation': true,
									'allRequired': false,
									'wrongActivity': false,
									'accreditation': false, //accreditation
								}
								
							});


						});	
					};
				} else {
					$scope.loginValidation = {
					'validation': true,
					'emailConfirm': true
					}
				}

				


				// if(userInfo.password.length < 6 || userInfo.password.length == null) {
					
				// 	$scope.loginValidation = {
				// 		'validation': true,
				// 		'allRequired': false,
				// 		'wrongActivity': false,
				// 		'accreditation': true, //accreditation
				// 	}

				// } else {
							
					
				// 	registerUser.register({
				// 		"meetingId": userInfo.meetingId,
				// 		"login": userInfo.email,
				// 		"password": userInfo.password,
				// 		"accreditation": userInfo.password,
				// 		"accreditationType": acctype,
				// 		"firstName": userInfo.fname,
				// 		"lastName": userInfo.lname,
				// 		"email": userInfo.email,
				// 		"state": userInfo.state.state,
				// 		"langKey": "en",
				// 		"roles": [
				// 		""
				// 		]
				// 	}).then(function (credentials) {
						
				// 		Auth.login({
				// 			username: credentials.email,
				// 			password: credentials.password,
				// 			//rememberMe: $scope.rememberMe
				// 		}).then(function () {
				// 			$scope.authenticationError = false;

				// 			if ($rootScope.previousStateName === '/') {
				// 				$state.go('myModules');
				// 			} else {
					
				// 				$state.go('myModules');
				// 			}
				// 		}).catch(function () {
				// 			$scope.authenticationError = true;
							
				// 			$scope.loginValidation = {
				// 				'validation': true,
				// 				'allRequired': false,
				// 				'wrongActivity': false,
				// 				'accreditation': false, //accreditation
				// 			}
							
				// 		});


				// 	});	
				// }

				

			} else {

				$scope.loginValidation = {
					'validation': true,
					'allRequired': false,
					'wrongActivity': true,
					'accreditation': false, //accreditation
				}

				// $scope.validation = true;
				// $scope.allRequired = false;
				// $scope.wrongActivity = true;
				
			}
			

			} else {
				$scope.loginValidation = {
					'validation': true,
					'allRequired': true,
					'wrongActivity': false,
					'accreditation': false, //accreditation
				}

				// $scope.validation = true;
				// $scope.allRequired = true;
				$scope.userForm.submitted = true;
			}
		}	
}]);
