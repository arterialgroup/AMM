'use strict';

angular.module('arterialeduApp')
    .factory('Attendee', function ($resource) {
        return $resource('api/attendees/:id', {}, {
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
