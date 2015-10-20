'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('moderator-dashboard', {
                parent: 'site',
                url: '/moderator-dashboard',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/moderator-ui/partials/moderator-dashboard.html',
                        controller: 'ModeratorMainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userResponse', {
                parent: 'moderator-dashboard',
                url: '/user-response/:activity/:id',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/moderator-ui/partials/user-response.html',
                        controller: 'UserResponseController' 
                    }
                },
                resolve: {
                    userId: function($stateParams) {
                        return $stateParams.id;
                    },
                    activityId: function($stateParams) {
                        return $stateParams.activity;
                    },
                     translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user-response');
                        return $translate.refresh();
                    }]
                }
            });

    });
