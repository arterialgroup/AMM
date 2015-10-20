'use strict';

angular.module('arterialeduApp')
.factory('ProgressService', ['$rootScope', 'MyLearningSection', 'moduleId', function ($rootScope, MyLearningSection, moduleId){


	return {

		sections: MyLearningSection.query({moduleId: moduleId}),
		log: console.log(sections)

		//sections: console.log('working')
		// get current user account 
		// $scope.account = Account.get();
		// var data = $scope.account;

		// var createDate = new Date();
		// var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();

		// console.log(formatDate);

		// $scope.activityModule = {
		// 	createUserModule: function() {
		// 		console.log(data.login);
		// 		var userLogin = data.login;

		// 		$scope.user = UserHttp.getCurrentUser(userLogin);
		// 		var userData = $scope.user;

		// 		userData.then(function(data) {

		// 			function create() {
		// 				UserModule.update(
		// 				{
		// 					"id": 0,
		// 					"module": {
		// 						"id": 1

		// 					},
		// 					"user": {
		// 						"id": data.data.id,

		// 						"activated": data.data.activated
		// 					}
		// 				}
		// 				);
		// 			}

		// 			return create();
		// 		});	
		// 		console.log(this.startProgress);
		// 		this.startProgress();
		// 	},
		// 	startProgress: function() {
		// 		console.log('inside');


		// 		UserProgressHttp.startProgress(
		// 		{
		// 			"userModuleId": 54,
		// 			"stepId": 1,
		// 			"startDate": formatDate,
		// 			"endDate": ""

		// 		}
		// 		);

		// 	}
		// }

	}


}]);
