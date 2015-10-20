'use strict';

angular.module('arterialeduApp')
.factory('MeetingHttp', function ($http) {
    var Meeting = {
        getMeetings: function() {
            return $http.get('api/meetings');
        },
        getMeetingById: function(id) {
            return $http.get('api/meetings/' + id);
        },
    };
    return Meeting;
});

