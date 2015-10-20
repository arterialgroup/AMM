'use strict';


angular.module('arterialeduApp')
.controller('ModeratorDashboardMainController', ['$scope', 'MeetingHttp', 'userModule', function ($scope, MeetingHttp, userModule){
	var pageNum = 1;
	
	$scope.loadMore = function () {
		
		pageNum++;
		
	}

	$scope.allMeetings = MeetingHttp.getMeetings();
	$scope.allUserModules = userModule.getUserModule(pageNum);

	var meetingAttendessArray = [];
	$scope.meetingAttendess = meetingAttendessArray;

	$scope.allMeetings.then(function (repsonse) {
		
		$scope.findMeeting = function (meetingInfo) {

			$scope.selectedActivity = meetingInfo.meetingID;
			
			if ($scope.moderatorDashboardForm.$valid) {
				$scope.moderatorDashboardForm.submitted = false;
				$scope.moderatorDashboardForm.submitted = false;

				$scope.allMeetings.then(function (repsonse) {
					
					angular.forEach(repsonse.data, function(value, key){
						if (value.activityId === parseInt($scope.selectedActivity)) {
							$scope.activityId = value.id;
						};
					});
					return $scope.activityId; 
				}).then(function (activityId) {
					
					$scope.allUserModules.then(function (data) {
						
						angular.forEach(data.data, function(value, key){
							if (value.module.id === activityId) {
								meetingAttendessArray.push(value.user);
							}							
						});
						
						$scope.foundAttendess = true;

					})
				})

			}
		};
	});

}]);