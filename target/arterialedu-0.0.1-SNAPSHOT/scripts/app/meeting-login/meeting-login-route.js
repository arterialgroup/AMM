'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('meetingLoginPage', {
                parent: 'meetingLogin',
                url: '/',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.meetingLogin.main'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/meeting-login/partials/meetingLogin.html',
                        controller: 'MeetingLoginController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
