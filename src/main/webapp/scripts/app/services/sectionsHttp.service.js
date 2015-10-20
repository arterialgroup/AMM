'use strict';

angular.module('arterialeduApp')
.factory('SectionsHttp', function ($http) {
    var Sections = {
        getSectionsById: function(id) {
            return $http.get('api/sections/' + id);
        }
    };
    return Sections;
});
