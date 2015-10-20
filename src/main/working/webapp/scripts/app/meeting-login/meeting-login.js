angular.module('arterialeduApp')
	.run(function ($rootScope, $location, $window, $http, $state, $translate, Auth, Principal, AccountHttp, Language, ENV, VERSION) {
		
		
		// if (Principal.identity(force)) {
  //               //Auth.authorize();
  //               console.log('something..');
  //       } else {
  //       	console.log('no role');
  //       }
	})
    .config(function ($stateProvider) {
        $stateProvider
            .state('meetingLogin', {
                abstract: true,
                parent: 'site'
            });
    });