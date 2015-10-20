'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('module', {
                parent: 'entity',
                url: '/module',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'arterialeduApp.module.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/module/modules.html',
                        controller: 'ModuleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('module');
                        return $translate.refresh();
                    }]
                }
            })
            .state('moduleDetail', {
                parent: 'entity',
                url: '/module/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'arterialeduApp.module.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/module/module-detail.html',
                        controller: 'ModuleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('module');
                        return $translate.refresh();
                    }]
                }
            });
    });
