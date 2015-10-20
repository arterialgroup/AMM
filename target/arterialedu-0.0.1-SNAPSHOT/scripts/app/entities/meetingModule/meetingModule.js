'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('meetingModule', {
                parent: 'entity',
                url: '/meetingModule',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meetingModule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meetingModule/meetingModules.html',
                        controller: 'MeetingModuleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meetingModule');
                        return $translate.refresh();
                    }]
                }
            })
            .state('meetingModuleDetail', {
                parent: 'entity',
                url: '/meetingModule/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meetingModule.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meetingModule/meetingModule-detail.html',
                        controller: 'MeetingModuleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meetingModule');
                        return $translate.refresh();
                    }]
                }
            });
    });
