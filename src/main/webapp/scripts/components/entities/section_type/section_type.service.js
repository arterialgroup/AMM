'use strict';

angular.module('arterialeduApp')
    .factory('Section_type', function ($resource) {
        return $resource('api/section_types/:id', {}, {
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
