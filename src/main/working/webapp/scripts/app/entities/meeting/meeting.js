'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('meeting', {
                parent: 'entity',
                url: '/meeting',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meeting.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meeting/meetings.html',
                        controller: 'MeetingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meeting');
                        return $translate.refresh();
                    }]
                }
            })
            .state('meetingDetail', {
                parent: 'entity',
                url: '/meeting/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.meeting.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/meeting/meeting-detail.html',
                        controller: 'MeetingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('meeting');
                        return $translate.refresh();
                    }]
                }
            });
    });
