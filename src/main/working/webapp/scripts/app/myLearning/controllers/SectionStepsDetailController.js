// 'use strict';

angular.module('arterialeduApp')
.controller('SectionStepsDetailController', ['$rootScope', '$scope', '$state', 'userResponseHttp', '$controller', 'Step', 'StepHttp', 'QuestionByStep', 'QuestionsHttp', 'UserProgressHttp', 'stepId', 'UserHttp', 'AccountHttp', 'userModuleHttp', '$stateParams', 'ServiceIdService', function ($rootScope, $scope, $state, userResponseHttp, $controller, Step, StepHttp, QuestionByStep, QuestionsHttp, UserProgressHttp, stepId, UserHttp, AccountHttp, userModuleHttp, $stateParams, ServiceIdService) {

	

	/*	----------------------------------------------------------------------------------------------------
	 *	getting step by step id */

	$scope.steps = Step.get({id: stepId});
	var step = $scope.steps;


	StepHttp.getStep(stepId).then(function (data) {
		console.log('steps........: ',data.data.section.id);
		$scope.sectionId = data.data.section.id;
	});


	var isCheckBox = false;
	var isRadio = false;
	var isFreeText = false;
	var isDropDown = false; 
	var isGroup = false;
	var userModelIdGlobal = false

	// My checks 
	console.log('Top of page \n isCheckBox: ',isCheckBox +
				'\n isRadio: ', isRadio +
				'\n isFreeText: ',isFreeText +
				'\n isGroup: ',isGroup +
				'\n isDropDown: ',isDropDown);	

	// questions by setep
	$scope.questionsReturned = QuestionByStep.query({id: stepId});

	// question and answers - using http to use promise functions
	$scope.qanda = QuestionsHttp.getQuestionByStep(stepId);
	console.log('$scope.qanda: ',$scope.qanda);

	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	function getUserModule() {
		accounthttp.then(function(data) {
			// var accountUserLogin = data.data.id
			getUserModuleById(data.data.id);
			// var userObject = UserHttp.getCurrentUser(accountUserLogin);
			//return userObject;

		});
		// .then(function(data) {

		// 	return data.data
		// }).then(function(data) {

		// 	getUserModuleById(data.id);
		// });

		function getUserModuleById(data) {
			var returnUserModuleById = userModuleHttp.userByUserId(data);

			returnUserModuleById.then(function(data) {

				return data.data;
			}).then(function(data) {

				for(var prop in data) {
					getUpdateUserProgress(data[prop].id);
					return data[prop].id;
				}
			});
		}

		function getUpdateUserProgress(id) {
			var UserProgressReturned = UserProgressHttp.getProgress(id);


			UserProgressReturned.then(function(data) {
				userModelIdGlobal = data.data.userModuleId;
				userModelIdGlobal = userModelIdGlobal;
				//console.log('userModelIdGlobal: ',userModelIdGlobal);
			});
		}
	}
	getUserModule();


	/*	----------------------------------------------------------------------------------------------------
	 *	Step navigation */

	// steps array for step navigation
	var stepsNavArray = [];
	$scope.stepsNavArray = stepsNavArray;


	// getting section by section id and pushing stepsNavArray to var
	var sectionId = ServiceIdService.setId;
	var stepsNavArrayBySectionId = StepHttp.getStepBySection(sectionId);
	
	stepsNavArrayBySectionId.then(function(data) {
		for(var prop in data.data) {
			stepsNavArray.push(data.data[prop].id);
		}
		//console.log($scope.stepsNavArray);
		var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
		// console.log('stepsArray: ',$scope.stepsNavArray);

		// console.log('length: ',$scope.stepsNavArray.length);
		// console.log('$scope.sectionId: ',$scope.sectionId);

		// console.log('$scope.stepsNavArray.indexOf(1): ',$scope.stepsNavArray.indexOf($scope.sectionId));
		console.log('$scope.stepsNavArray: ',$scope.stepsNavArray);
		

		var currentPosition = $scope.stepsNavArray.indexOf(parseInt(stepId));
		var totalPercentage = $scope.stepsNavArray.length;
	
		// (Completed Task/Total Task ) * 100
		var calculateComplete = function(postion,total) {
			var result = (postion/total) * 100;
			console.log('result: ',Math.round(result));
			$scope.percentageComplete = Math.round(result) + '%';
			console.log('$scope.percentageComplete: ',$scope.percentageComplete);
		}

		calculateComplete(currentPosition,totalPercentage);

		// if last
		if($stateParams.id == lastArrayItem) {
			//console.log('working true');
			$scope.showMe = {
				'submit': false,
				'next': false, 
				'complete': true,
			};
		} else {
			//console.log('working false');
			$scope.showMe = {
				'submit': false,
				'next': true, 
				'complete': false, 
			};
		}
		//console.log('stepsNavArray: ',$scope.stepsNavArray[0]);
		//console.log('$stateParams.id: ',$stateParams.id);
		if($stateParams.id == stepsNavArray[0]) {
			//console.log('we are equal.............');
			$scope.prevShow = false;
		} else {
			$scope.prevShow = true;
		}
	});  



	var lastIndex = function(data) {
		//console.log(data);
		//console.log($scope.stepsNavArray);
	}
	lastIndex(stepsNavArray);
	
	// step navigation 
	function getPageName() {
		var currentStep = $stateParams.id;
		var currentStepId = parseInt(currentStep);
		return currentStepId;
	}

	


	// $scope.next = function() {	
	// 	console.log('in next');
	// 	//console.log('here: ',$scope.questionForm);
	// 	// if (!$scope.questionForm.$pristine) {
	// 		var currentStep = getPageName();	  
	// 		var idx = stepsNavArray.indexOf(currentStep);

	// 			if(idx != -1 && idx < (stepsNavArray.length-1)) {
	// 				var newId = stepsNavArray[idx+1];
	// 				$state.go('sectionStepDetail', { id : newId });
	// 			}	
	// 		} 
			// else {
			// 	console.log('next apge FAIL');
			// 	$scope.questionForm.submitted = true;
			// }
	

	// console.log('state: ',$stateParams.id);
	// $scope.next = function() {	
	// 	//console.log('in next');
	// 	//	console.log('in valid');
	// 		var currentStep = getPageName();	  
	// 		var idx = stepsNavArray.indexOf(currentStep);

	// 		if(idx != -1 && idx < (stepsNavArray.length-1)) {
	// 			var newId = stepsNavArray[idx+1];
	// 			$state.go('sectionStepDetail', { id : newId });
	// 		}

	// }
	


	

	
	/*	----------------------------------------------------------------------------------------------------
	 *	Progress tracking  */

	var createDate = new Date();
	var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();

	// $scope.activityModule = {

	// 	updateProgress: function() {

	// 		// var getUserId = function() {

	// 		// 	/* Get account object */
	// 		// 	accounthttp.then(function(data) {
	// 		// 		var accountUserLogin = data.data.login

	// 		// 		/* get and return user resource */
	// 		// 		var userObject = UserHttp.getCurrentUser(accountUserLogin);
	// 		// 		return userObject;

	// 		// 	}).then(function(data) {
	// 		// 		/* get user obj */
	// 		// 		return data.data
	// 		// 	}).then(function(data) {
	// 		// 		/* call getUserById with user id */
	// 		// 		getUserModuleById(data.id);
	// 		// 	});
	// 		// };


	// 		// var getUserModuleById = function(data) {

	// 		// 	/* returned userModule */
	// 		// 	var returnUserModuleById = userModuleHttp.userByUserId(data);

	// 		// 	returnUserModuleById.then(function(data) {
	// 		// 		 userModule obj 
	// 		// 		return data.data;
	// 		// 	}).then(function(data) {
	// 		// 		/* return id from obj array */
	// 		// 		for(var prop in data) {
	// 		// 			getUpdateUserProgress(data[prop].id);
	// 		// 			return data[prop].id;
	// 		// 		}
	// 		// 	});
	// 		// };

	// 		var getUpdateUserProgress = function(id) {
	// 			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
	// 			console.log('lastArrayItem: ',lastArrayItem);
	// 			/* get current userModule progress */
	// 			var UserProgressReturned = UserProgressHttp.getProgress(id);


	// 			UserProgressReturned.then(function(data) {

	// 				/* update */
	// 				function update() {

	// 					UserProgressHttp.updateProgress(
	// 					{
	// 						"userProgressId": data.data.userProgressId,
	// 						"userModuleId": data.data.userModuleId,
	// 						"stepId": stepId,
	// 						"startDate": "",
	// 						"endDate": ""
	// 					}
	// 					);
	// 				}
	// 				update();

	// 				if(lastArrayItem == $stateParams.id) {
	// 					console.log('lastArrayItem is: ',lastArrayItem);
	// 					function end() {
	// 						UserProgressHttp.endProgress(
	// 						{
	// 							"userProgressId": data.data.userProgressId,
	// 							"userModuleId": data.data.userModuleId,
	// 							"stepId": stepId,
	// 							"startDate": "",
	// 							"endDate": formatDate
	// 						}
	// 						);
	// 					} 
	// 					return end();	
	// 				}



	// 			});
	// 		};

	// 		getUpdateUserProgress(userModelIdGlobal);

	// 	}

	// };


	/*	----------------------------------------------------------------------------------------------------
	 *	question and answer */

	 // getting question objects
	var qanda = $scope.qanda;

	// arrays to push new collections 
	var answerCollection = [];
	var correctAnswerCollection = [];
	var questionCollection = [];
	var groupQuestionCollection = [];
	var groupAnswerCollection = [];

	$scope.questionCollection = questionCollection;
	$scope.groupQuestionCollection = groupQuestionCollection;
	$scope.answerCollection = answerCollection;
	$scope.correctAnswerCollection = correctAnswerCollection;
	$scope.groupAnswerCollection = groupAnswerCollection;

	// getting and making new question collection
	qanda.then(function(data) {
		//console.log('INSDE......');

		console.log('qanda data.data....',data);
		console.log('ISGROUP....',isGroup);


		isGroup = data.data[0].isGroup;

		

		var questionObj = data.data;


		angular.forEach(questionObj[0].questions, function(value){
			console.log(value);
			if( value.questionType === 'CHECKBOX' ) {
				console.log('checkbox');
				$scope.isCheckBox = true;
				isCheckBox = true;
			} else if( value.questionType === 'RADIO' ) {
				console.log('radio');
				$scope.isRadio = true;
				isRadio = true;
			} else if( value.questionType === "FREETEXT" ) {
				console.log('freetext');
				$scope.isFreeText = true;
				isFreeText = true
			} else if( value.questionType === 'DROPDOWN' ) {
				console.log('dropdown');
				$scope.isDropDown = true;
				isDropDown = true;
			}

		});

		// single question
		if(!isGroup ) {
			console.log('insdie !isGroup if');
			$scope.isGroup = isGroup;
			

			// questions
			angular.forEach(questionObj, function(value){
				var question = value.questions[0];
				questionCollection.push(question);
			});

			// answer 
			angular.forEach(questionObj, function(value){
				var answerObj = value.questions[0].answers;
				
				angular.forEach(answerObj, function(value){
					answerCollection.push(value);

					if(value.correct === true) {
						correctAnswerCollection.push(answerObj);					
					}

					$scope.getTotal = function(){
						var total = 0;
						var totalArray = [];
						$scope.totalArray = totalArray;
						// var colAmount = [];
						// $scope.colAmount = colAmount;

						for(var i = 0; i < $scope.answerCollection.length; i++){
							var item = $scope.answerCollection[i];
							// total += item;
							totalArray.push(item);
						}
						//console.log('$scope.totalArray: ',$scope.totalArray);
						if($scope.totalArray.length == 4) {
							var colAmount = 3;
							$scope.colAmount = colAmount;
							//console.log('in if $scope.colAmount: ',$scope.colAmount);
							$scope.colValue = 3;
							return '3';
						} else if ($scope.totalArray.length == 5) {
							$scope.colValue = 5;
							return '5';
						}

						//console.log('not if: ',$scope.colAmount);
						//return $scope.colAmount;
					};
					
				});

			});

		// group question
		
		} else if( $scope.isFreeText ) {
			console.log('insdie isFreeText if');
			angular.forEach(questionObj, function(value) {
				
				$scope.groupQuestionTitle = value.groupText;
				//console.log('value: ',$scope.groupQuestionTitle);
			});

			// getting group questions question text
			angular.forEach(questionObj, function(value) {
				$scope.groupQuestionTitle = value.groupText;
			});

			angular.forEach(questionObj, function(value){
				var groupQuestions = value.questions;

				angular.forEach(groupQuestions, function(value){
					var groupQuestionObj = value;

					groupQuestionCollection.push(groupQuestionObj);
				}); 
			});

			// getting group answer text
			angular.forEach(questionObj, function(value){
				var groupAnswers = value.questions[0].answers;
				// console.log('value',value.questions[0].answers);

				angular.forEach(groupAnswers, function(value){
					// console.log('value: ',value);
					var groupAnswersObj = value;
					groupAnswerCollection.push(groupAnswersObj);
					console.log('RETURNED DATA ANSWER: ',$scope.groupAnswerCollection);
				}); 
			});

		}  else if( isGroup ) {

			$scope.isGroup = isGroup;

			// getting group questions question text
			angular.forEach(questionObj, function(value) {
				$scope.groupQuestionTitle = value.groupText;
			});

			angular.forEach(questionObj, function(value){
				var groupQuestions = value.questions;

				angular.forEach(groupQuestions, function(value){
					var groupQuestionObj = value;

					angular.forEach(groupQuestionObj, function(value, key){
						//console.log('value of questions: ',value);
					});
					
					groupQuestionCollection.push(groupQuestionObj);
				}); 
			});

			// getting group answer text
			angular.forEach(questionObj, function(value){
				var groupAnswers = value.questions[0].answers;
				// console.log('value',value.questions[0].answers);

				angular.forEach(groupAnswers, function(value){
					// console.log('value: ',value);
					var groupAnswersObj = value;
					groupAnswerCollection.push(groupAnswersObj);
					console.log('RETURNED DATA ANSWER: ',$scope.groupAnswerCollection);
					$scope.sortName = 'answerId';
				});
			});
		}



	});
	

	/*	----------------------------------------------------------------------------------------------------
	 *	submit answer */

	//$scope.currentAnswer = {};
	$scope.answer = {};

	$scope.answerBtn = {
		'result': 'rectangle-radio-btn'
	}

	// submitAnswer function

	$scope.submitted = false;


	$scope.formData = {};
	$scope.currentAnswer = {};
	console.log('global: ',$scope.currentAnswer);
	$scope.showMe = false;




	$scope.prevPage = function() {
		// var prevPage = function() {
			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
			console.log('lastArrayItem: ',lastArrayItem);
			var currentStep = getPageName();	  
			var idx = stepsNavArray.indexOf(currentStep);

				if(idx != -1) {
					var newId = stepsNavArray[idx-1];
					console.log('newId: ',newId);
					$state.go('sectionStepDetail', { sectionId : $scope.sectionId, id : newId });
				} 

	}

	$scope.submitAnswer = function(answer) {
		
		console.log('in submit: ',$scope.currentAnswer);
		$scope.currentAnswer = answer;

		console.log('answer: ',answer);

		//var answerObj = answer;

		console.log('userModelIdGlobal: ',userModelIdGlobal);


				/* save response */
				var getUpdateUserProgress = function(id) {
				var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
				console.log('lastArrayItem: ',lastArrayItem);
				/* get current userModule progress */
				var UserProgressReturned = UserProgressHttp.getProgress(id);


				UserProgressReturned.then(function(data) {

					/* update */
					function update() {

						UserProgressHttp.updateProgress(
						{
							"userProgressId": data.data.userProgressId,
							"userModuleId": data.data.userModuleId,
							"stepId": stepId,
							"startDate": "",
							"endDate": ""
						}
						);
					}
					update();


					// this will need to be moved to the confirm button
					// if(lastArrayItem == $stateParams.id) {
					// 	console.log('lastArrayItem is: ',lastArrayItem);
					// 	function end() {
					// 		UserProgressHttp.endProgress(
					// 		{
					// 			"userProgressId": data.data.userProgressId,
					// 			"userModuleId": data.data.userModuleId,
					// 			"stepId": stepId,
					// 			"startDate": "",
					// 			"endDate": formatDate
					// 		}
					// 		);
					// 	} 
					// 	return end();	
					// }



				});

				/* END next page and update */
			};

			var nextPage = function() {
			var currentStep = getPageName();	  
			var idx = stepsNavArray.indexOf(currentStep);

				if(idx != -1 && idx < (stepsNavArray.length-1)) {
					var newId = stepsNavArray[idx+1];
					$state.go('sectionStepDetail', { sectionId : $scope.sectionId, id : newId });
				} else if (idx != -1 && idx >= (stepsNavArray.length-1)) {
					$state.go('confirm', { id : $scope.sectionId });
				}
			}
			

					
				
		

		if($scope.answers.$valid) {
			$scope.showMe = false;
			console.log('we hav an answer');
			//console.log('answerObj: ',answerObj);

			console.log('ANSWER IS... ',answer);
			console.log('userModelIdGlobal: ',userModelIdGlobal);

		// if ($scope.questionForm.$pristine) {
			console.log('yes wrokgin');
			var answerData = $scope.currentAnswer;
			console.log('answerData: ',answerData);
			var answerArray = [];

			
			/* Record userResponse */
			if(isRadio && !isGroup) {
				console.log('insdie if: ',answerData);
				console.log('INNER userModelIdGlobal: ',userModelIdGlobal);
				console.log('INNER answerData: ',answerData);
				console.log('INNER answerData: ',answerData);


				// userResponseHttp.saveResponse(
				// {
				// 	"userModuleId": userModelIdGlobal,
				// 	"stepId": stepId,
				// 	"answers": [
				// 	{
				// 		"questionId": answerData.questionId,
				// 		"answerId": answerData.answerId,
				// 		"answerText": answerData.answerText,
				// 		"correct": false
				// 	}
				// 	]
				// }
				// );

				angular.forEach(answerData, function(value, key){
									console.log('KEY: ',key);
									console.log('VALUE: ',value);
									answerArray.push({
										"questionId": value.questionId,
										"answerId": value.answerId,
										"answerText": value.answerText,
										"correct": false
									});
									console.log('answerArray: ',answerArray);
								});	

								userResponseHttp.saveResponse(
								{
									"userModuleId": userModelIdGlobal,
									"stepId": stepId,
									"answers": answerArray
								}
								);








			getUpdateUserProgress(userModelIdGlobal);
			nextPage();

							


			} else if (isRadio && isGroup) {

				console.log('HERE WE ARE..............');
				console.log('insdie if..........: ',answerData);								



				getUpdateUserProgress(userModelIdGlobal);
				nextPage();



		// 		/* next page */
		// 			console.log('in next');
		// //console.log('here: ',$scope.questionForm);
		// // if (!$scope.questionForm.$pristine) {
		// 	var currentStep = getPageName();	  
		// 	var idx = stepsNavArray.indexOf(currentStep);

		// 		if(idx != -1 && idx < (stepsNavArray.length-1)) {
		// 			var newId = stepsNavArray[idx+1];
		// 			$state.go('sectionStepDetail', { id : newId });
		// 		}

				
				

				angular.forEach(answerData, function(value, key){
					console.log('KEY: ',key);
					console.log('VALUE: ',value);
					answerArray.push({
						"questionId": key,
						"answerId": value.answerId,
						"answerText": value.answerText,
						"correct": false
					});
					console.log('answerArray: ',answerArray);
				});	

				userResponseHttp.saveResponse(
				{
					"userModuleId": userModelIdGlobal,
					"stepId": stepId,
					"answers": answerArray
				}
				);




			} else if (isFreeText) {

												/* next page */
		// 			console.log('in next');
		// //console.log('here: ',$scope.questionForm);
		// // if (!$scope.questionForm.$pristine) {
		// 	var currentStep = getPageName();	  
		// 	var idx = stepsNavArray.indexOf(currentStep);

		// 		if(idx != -1 && idx < (stepsNavArray.length-1)) {
		// 			var newId = stepsNavArray[idx+1];
		// 			$state.go('sectionStepDetail', { id : newId });
		// 		}



				getUpdateUserProgress(userModelIdGlobal);
				nextPage();


				angular.forEach(answerData, function(value, key){

					answerArray.push({
						"questionId": key,
						"answerId": "",
						"answerText": value,
						"correct": false
					});
				});	

				userResponseHttp.saveResponse(
				{
					"userModuleId": userModelIdGlobal,
					"stepId": stepId,
					"answers": answerArray
				}
				);	
			}; 


			/* FOR ANSWER FEEDBACK */
			$scope.disableInput = true;

			// setting the users answer
			var usersResult = answer.correct;
			var alert = answer.alert;

			if(usersResult) {
				$scope.answerBtn = {
					'result': 'rectangle-radio-btn'
				}
				$scope.answer = { 
					'answer': 'Correct',
					'show': false
				}
				$scope.alert = { 
					'alert': 'alerts alerts-success',
					'info': 'alerts alerts-info' 
				}
				console.log(alert);
		} else {



			$scope.answerBtn = {
				'result': 'rectangle-radio-btn-incorrect'
			}
			$scope.answer = { 
				'answer': 'Incorrect',
				'show': false // this is in place to not show feedback yet
			}
			$scope.alert = { 
				'alert': 'alerts alerts-danger',
				'info': 'alerts alerts-info'
			}
			console.log(alert);
		}
		} else {
			console.log('we do not have a answer');
			$scope.testScope = true;
			$scope.answers.submitted = true;
		}
		

	}

	
}]).directive('subScript', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        replace: true,
        scope: {
            props: '=subScript',
            ngModel: '=ngModel'
        },
        link: function compile(scope, element, attrs, controller) {
            scope.$watch('ngModel', function (value) {

            	if (value == 'CHADS2') {
            		var html = '<strong>CHADS<sub>2</sub></strong>';
                	element.html(html);
            	};
            	if (value == 'CHA2DS2VASc') {
            		var html = '<strong>CHA<sub>2</sub>DS<sub>2</sub>VASc</strong>';
                	element.html(html);
            	};
            });
        }
    };	
});













