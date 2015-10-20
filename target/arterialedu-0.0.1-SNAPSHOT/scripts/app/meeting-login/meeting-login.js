angular.module('arterialeduApp')
	.run(function ($rootScope, $location, $window, $http, $state, $translate, Auth, Principal, AccountHttp, Language, ENV, VERSION) {
		
		

	})
    .config(function ($stateProvider) {
        $stateProvider
            .state('meetingLogin', {
                abstract: true,
                parent: 'site'
            });
    });