'use strict';

angular.module('arterialeduApp')
.factory('UserHttp', function ($http) {

   var myUserHttp = {
       getCurrentUser: function(id) {
           return $http.get('api/users/' + id);
       }
   };

   return myUserHttp;
});
