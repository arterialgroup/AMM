'use strict';

angular.module('arterialeduApp')
.factory('userResponseHttp', function ($http) {
    var response = {
        saveResponse: function(data) {
            
            
            $http({
                "url": '/api/user/response/save',
                "dataType": 'application/json',
                "method": 'POST',
                "data": data 
            }).success(function(data, response){
                
                
            }).error(function(data, error){
                
            });

        },
        getSavedResponse: function(userModule, stepId) {
             return $http.get('api/user/response/byusermoduleforstep/' + userModule + '?stepId=' + stepId);
        }
    };
    return response;
});