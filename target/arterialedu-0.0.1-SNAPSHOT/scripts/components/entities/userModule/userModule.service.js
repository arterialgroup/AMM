'use strict';

angular.module('arterialeduApp')
    .factory('UserModule', function ($resource) {
        return $resource('api/userModules/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
