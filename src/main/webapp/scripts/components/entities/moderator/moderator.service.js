'use strict';

angular.module('arterialeduApp')
    .factory('Moderator', function ($resource) {
        return $resource('api/moderators/:id', {}, {
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
