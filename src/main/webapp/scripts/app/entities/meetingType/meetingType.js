'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('meetingType', {
                parent: 'entity',
                url: '/meetingType',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meetingType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meetingType/meetingTypes.html',
                        controller: 'MeetingTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meetingType');
                        return $translate.refresh();
                    }]
                }
            })
            .state('meetingTypeDetail', {
                parent: 'entity',
                url: '/meetingType/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meetingType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meetingType/meetingType-detail.html',
                        controller: 'MeetingTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meetingType');
                        return $translate.refresh();
                    }]
                }
            });
    });
