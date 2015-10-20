'use strict';

angular.module('arterialeduApp')
.controller('ProgressController', ['$scope', 'MyLearningSection', 'UserModule', 'Account', 'UserHttp', 'UserProgressHttp', 'moduleId', function ($scope, MyLearningSection, UserModule, Account, UserHttp, UserProgressHttp, moduleId){

	$scope.sections = MyLearningSection.query({moduleId: moduleId});
	

// get current user account 
$scope.account = Account.get();
var data = $scope.account;

var createDate = new Date();
var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();



$scope.activityModule = {
	createUserModule: function() {
		
		var userLogin = data.login;

		$scope.user = UserHttp.getCurrentUser(userLogin);
		var userData = $scope.user;

		userData.then(function(data) {

			function create() {
				UserModule.update(
				{
					"id": 0,
					"module": {
						"id": 1

					},
					"user": {
						"id": data.data.id,

						"activated": data.data.activated
					}
				}
				);
			}

			return create();
		});	
		
		this.startProgress();
	},
	startProgress: function() {
		


		UserProgressHttp.startProgress(
		{
			"userModuleId": 54,
			"stepId": 1,
			"startDate": formatDate,
			"endDate": ""

		}
		);

	}
}

}]);
