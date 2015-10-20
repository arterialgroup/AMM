'use strict';

angular.module('arterialeduApp')
.factory('meetingModulesHttp', function ($http) {
    var meetingModules = {
        getMeetingModules: function() {
            return $http.get('api/meetingModules');
        }       
    };
    return meetingModules;
});

