'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userModule', {
                parent: 'entity',
                url: '/userModule',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.userModule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userModule/userModules.html',
                        controller: 'UserModuleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userModule');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userModuleDetail', {
                parent: 'entity',
                url: '/userModule/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.userModule.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userModule/userModule-detail.html',
                        controller: 'UserModuleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userModule');
                        return $translate.refresh();
                    }]
                }
            });
    });
