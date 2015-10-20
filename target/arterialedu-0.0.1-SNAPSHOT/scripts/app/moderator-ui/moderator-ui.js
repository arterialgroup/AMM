angular.module('arterialeduApp')
	.run(function ($rootScope, $location, $window, $http, $state, $translate, Auth, Principal, AccountHttp, Language, ENV, VERSION) {
		
		

})
    .config(function ($stateProvider) {
        $stateProvider
            .state('moderatorMain', {
                abstract: true,
                parent: 'site'
            });
    });

// angular.module('arterialeduApp')
//     .config(function ($stateProvider) {
//         $stateProvider
//             .state('moderator', {
//                 abstract: true,
//                 parent: 'site'
//             });

//     });
