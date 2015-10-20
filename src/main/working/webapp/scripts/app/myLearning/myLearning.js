'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('myLearning', {
                abstract: true,
                parent: 'site'
            });

    });


