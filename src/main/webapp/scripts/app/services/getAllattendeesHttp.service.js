'use strict';

angular.module('arterialeduApp')
.factory('getAllAttendeesHttp', function ($http) {
    var Allattendees = {
    	getAllAttendees: function() {
            return $http.get('api/attendees');
        }
    };
    return Allattendees;
});

