'use strict';

angular.module('arterialeduApp')
.factory('AccountHttp', function ($http) {
    var Account = {
        getAccount: function() {
            return $http.get('api/account');
        }   
    };
    return Account;
});



// angular.module('arterialeduApp')
//     .factory('Account', function Account($resource) {
//         return $resource('api/account', {}, {
//             'get': { method: 'GET', params: {}, isArray: false,
//                 interceptor: {
//                     response: function(response) {
//                         // expose response
//                         return response;
//                     }
//                 }
//             }
//         });
//     });
