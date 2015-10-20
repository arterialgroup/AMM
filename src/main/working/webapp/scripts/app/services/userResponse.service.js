'use strict';

angular.module('arterialeduApp')
.factory('userResponseHttp', function ($http) {
    var response = {
        saveResponse: function(data) {
            console.log('first data: ',data);
            console.log('start');
            $http({
                "url": '/api/user/response/save',
                "dataType": 'application/json',
                "method": 'POST',
                "data": data 
            }).success(function(data, response){
                console.log('data: ',data);
                console.log(response);
            }).error(function(data, error){
                console.log(error);
            });

        }
    };
    return response;
});