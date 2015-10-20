'use strict';

angular.module('arterialeduApp')
.controller('MeetingLoginController', ['$rootScope', '$state', '$scope', 'registerUser', 'MeetingHttp', 'Auth', 'AccountHttp', function ($rootScope, $state, $scope, registerUser, MeetingHttp, Auth, AccountHttp) {
		
	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	accounthttp.then(function (data) {
		console.log(data.data);
		if (data.data) {
			console.log('User already logged in.\n Send them to required state');
			$state.go('myModules');
		};

	}, function (err) {
		console.log('No users is logged in');
	});

	$scope.submitted = false;
	$scope.activityId = '27893';
	$scope.loginValidation = {
		'submitted': false,
		'validation': false,
		'allRequired': false,
		'wrongActivity': false,
		'racgp': false //RACGP
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

	$scope.selected = { name: 'aSubItem' };

	// login function - We are checking if the form is logged in
	// then using validation and then if all ok logging in
	$scope.login = function(userInfo) {

		console.log('userInfo: ',userInfo);

		if ($scope.userForm.$valid) {

			if(userInfo.meetingId == '27893' ) {

				if(userInfo.password.length < 6 || userInfo.password.length == null) {
					console.log('less then 6');
					$scope.loginValidation = {
						'validation': true,
						'allRequired': false,
						'wrongActivity': false,
						'racgp': true, //RACGP
					}

				} else {
							console.log('over 6');
					console.log(userInfo);
					registerUser.register({
						"meetingId": userInfo.meetingId,
						"login": userInfo.email,
						"password": userInfo.password,
						"racgp": userInfo.password,
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
							console.log('we are in the catch');
							$scope.loginValidation = {
								'validation': true,
								'allRequired': false,
								'wrongActivity': false,
								'racgp': false, //RACGP
							}
							console.log('authenticationError: ',$scope.authenticationError);
						});


					});	
				}

				

			} else {

				$scope.loginValidation = {
					'validation': true,
					'allRequired': false,
					'wrongActivity': true,
					'racgp': false, //RACGP
				}

				// $scope.validation = true;
				// $scope.allRequired = false;
				// $scope.wrongActivity = true;
				console.log('wrong');
			}
			

			} else {
				$scope.loginValidation = {
					'validation': true,
					'allRequired': true,
					'wrongActivity': false,
					'racgp': false, //RACGP
				}

				// $scope.validation = true;
				// $scope.allRequired = true;
				$scope.userForm.submitted = true;
			}
		}	
}]);
