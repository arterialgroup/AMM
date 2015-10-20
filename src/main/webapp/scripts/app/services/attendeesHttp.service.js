'use strict';

angular.module('arterialeduApp')
.factory('AttendeesHttp', function ($http) {
    var Attendees = {
        getAttendeesById: function(id) {
            return $http.get('api/attendees/' + id);
        }
    };
    return Attendees;
});

