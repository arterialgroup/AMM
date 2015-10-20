'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('moduleType', {
                parent: 'entity',
                url: '/moduleType',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.moduleType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/moduleType/moduleTypes.html',
                        controller: 'ModuleTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('moduleType');
                        return $translate.refresh();
                    }]
                }
            })
            .state('moduleTypeDetail', {
                parent: 'entity',
                url: '/moduleType/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.moduleType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/moduleType/moduleType-detail.html',
                        controller: 'ModuleTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('moduleType');
                        return $translate.refresh();
                    }]
                }
            });
    });
