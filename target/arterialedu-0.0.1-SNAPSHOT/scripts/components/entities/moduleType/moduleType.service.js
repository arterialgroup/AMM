'use strict';

angular.module('arterialeduApp')
    .factory('ModuleType', function ($resource) {
        return $resource('api/moduleTypes/:id', {}, {
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
