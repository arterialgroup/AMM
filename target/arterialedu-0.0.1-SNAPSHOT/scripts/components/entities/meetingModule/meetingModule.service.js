'use strict';

angular.module('arterialeduApp')
    .factory('MeetingModule', function ($resource) {
        return $resource('api/meetingModules/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    var dateAvailableFrom = data.dateAvailable.split("-");
                    data.dateAvailable = new Date(new Date(dateAvailableFrom[0], dateAvailableFrom[1] - 1, dateAvailableFrom[2]));
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
