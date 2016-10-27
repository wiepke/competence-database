/**
 * Created by dehne on 24.10.2016.
 */


$(document).ready(function () {
    // Run code
    requirejs.config({
        //By default load any module IDs from js/lib
        baseUrl: '',
        //except, if the module ID starts with "app",
        //load it from the js/app directory. paths
        //config is relative to the baseUrl, and
        //never includes a ".js" extension since
        //the paths config could be for a directory.
        paths: {
            'superagent': 'node_modules/superagent/superagent'
        }
    });
    requirejs(['index'], function (main) {
        console.log(main);
        var apiClient = new main.ApiClient();
        //apiClient.basePath = 'http://fleckenroller.cs.uni-potsdam.de/app/competence-base';
        apiClient.basePath = 'http://localhost:8080/competence-base';
        var api = new main.DefaultApi(apiClient);
        console.log(apiClient);
        api.getCompetences({}, function (error, data, response) {
            console.log(error, data);
            console.log(response.body);
            document.getElementById('response').innerHTML = response.text;
        });

        // add a Competence to the api
        var competenceOpts = {
            "operator": "laufen",
            "catchwords": [
                "laufen", "gehen"
            ],
            "superCompetences": [],
            "subCompetences": [],
            "learningProjectName": "Test"
        }
        api.addCompetence("Meine TestKompetenz", {body: competenceOpts}, function (a, b, c) {
            console.log("competence created");
            document.getElementById('response2').innerHTML = c.statusText;
        });

        // real implementation

        // ...............................................................................
        $('#competenceCreateButton').click(function () {
            var value1 = $("#verbInput").val();
            var value2 = $("#verbInput2").val();
            var competenceString = "Die S. " + value1 + " " + $("#detailsInput").val() + " " + value2;
            var operator = value2 + value1;
            var catchwords = $('#tagsInput').tagsinput('items')
            // api call
            if (!Boolean(value1) || !Boolean(value2) || !Boolean(competenceString) || !Boolean(operator) || catchwords.length == 0) {
                $('#createSuccessMessage').hide();
                $('#createErrorMessage').show();
            }
            else {
                // competence prepared
                var competenceOpts2 = {
                    "operator": operator,
                    "catchwords": catchwords,
                    "superCompetences": [],
                    "subCompetences": [],
                    "learningProjectName": "Test"
                }
                // activities collection
                var activitiesSuggested = [];
                for (i = 0; i < courseModules.length; i++) {
                    if (modulesSelected[i]) {
                        var activity = {
                            "name": courseModules[i].name,
                            "qtip": "nicht definiert",
                            "icon": "nicht definiert",
                            "url": courseModules[i].url
                        };
                        activitiesSuggested.push(activity);
                        console.info(activity.name + "suggested");
                    }
                }
                ;

                api.addCompetence(competenceString, {body: competenceOpts}, function (a, b, c) {
                    if (c.statusText == "OK") {
                        console.log("competence created");
                        var courseBody = {
                            body: {
                                "courseId": "string",
                                "printableName": courseName,
                                "competences": [
                                    competenceString
                                ]
                            }
                        };
                        api.addCourse(courseId, courseBody, function (a, b, c) {
                            if (c.statusText == "OK") {
                                for (i = 0; i < activitiesSuggested.length; i++) {
                                    var activityToSend = activitiesSuggested[i];
                                    api.addActivity({body: activityToSend}, function (a, b, c) {
                                        for (i = 0; i < activitiesSuggested.length; i++) {
                                            var activityToSend2 = activitiesSuggested[i];
                                            api.addActivity_0(competenceString, activityToSend2, function (a, b, c) {
                                                console.info("activity has been added");
                                                $('#createSuccessMessage').show();

                                            });
                                        }
                                    });
                                }
                            } else {
                                $('#createErrorMessage').show();
                            }
                        });
                    } else {
                        $('#createErrorMessage').show();
                    }
                });
                // $('#createSuccessMessage').show();
                // ...............................................................................
            }
        });
    });
});
