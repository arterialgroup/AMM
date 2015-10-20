'use strict';

angular.module('arterialeduApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


