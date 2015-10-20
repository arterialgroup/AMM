'use strict';

angular.module('arterialeduApp')
    .factory('MeetingType', function ($resource) {
        return $resource('api/meetingTypes/:id', {}, {
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
