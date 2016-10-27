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
        apiClient.basePath = serverUrl;
        var api = new main.DefaultApi(apiClient);
        console.log(apiClient);


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

                api.addCompetence(competenceString, {body: competenceOpts2}, function (a, b, c) {
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
                                        for (y = 0; y < activitiesSuggested.length; y++) {
                                            var activityToSend2 = activitiesSuggested[y];
                                            api.addActivity_0(competenceString, activityToSend2, function (a, b, c) {
                                                var questions = $('#questionsArea').text().split(";");
                                                for (x=0;x<questions.length;x++) {
                                                    var question = {
                                                        "question": questions[x],
                                                        "competenceId": competenceString
                                                    };
                                                    api.addReflectiveQuestionToCompetence({"body": question}, function(a,b,c){
                                                        console.info("activity has been added");
                                                        $('#createSuccessMessage').show();
                                                    });
                                                }
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

        // lernziele bearbeiten

        api.getCompetences({"courseId": courseId}, function (error, data, response) {
            var competences = response.body;
            for (i = 0; i < competences.length; i++) {
                var competencePointed = competences[i];
                $('#competenceList').append("<li class=\"list-group-item\" id=\"competence"+i+"\">" + competencePointed + "</li>");
            }

            /// und noch UI stuff

            // http://bootsnipp.com/snippets/featured/checked-list-group
            $('.list-group.checked-list-box .list-group-item').each(function () {

                // Settings
                var $widget = $(this),
                    $checkbox = $('<input type="checkbox" class="hidden" />'),
                    color = ($widget.data('color') ? $widget.data('color') : "primary"),
                    style = ($widget.data('style') == "button" ? "btn-" : "list-group-item-"),
                    settings = {
                        on: {
                            icon: 'glyphicon glyphicon-check'
                        },
                        off: {
                            icon: 'glyphicon glyphicon-unchecked'
                        }
                    };

                $widget.css('cursor', 'pointer')
                $widget.append($checkbox);

                // Event Handlers
                $widget.on('click', function () {
                    $checkbox.prop('checked', !$checkbox.is(':checked'));
                    $checkbox.triggerHandler('change');
                    updateDisplay();
                });
                $checkbox.on('change', function () {
                    updateDisplay();
                });


                // Actions
                function updateDisplay() {
                    var isChecked = $checkbox.is(':checked');

                    // Set the button's state
                    $widget.data('state', (isChecked) ? "on" : "off");

                    // Set the button's icon
                    $widget.find('.state-icon')
                        .removeClass()
                        .addClass('state-icon ' + settings[$widget.data('state')].icon);

                    // Update the button's color
                    if (isChecked) {
                        $widget.addClass(style + color + ' active');
                    } else {
                        $widget.removeClass(style + color + ' active');
                    }
                }

                // Initialization
                function init() {

                    if ($widget.data('checked') == true) {
                        $checkbox.prop('checked', !$checkbox.is(':checked'));
                    }

                    updateDisplay();

                    // Inject the icon if applicable
                    if ($widget.find('.state-icon').length == 0) {
                        $widget.prepend('<span class="state-icon ' + settings[$widget.data('state')].icon + '"></span>');
                    }
                }

                init();
            });

        });

        // delete lernziele

        $('#competenceDeleteButton').on('click', function(event) {
            event.preventDefault();
            var checkedItems = {}, counter = 0;
            $("#competenceList li.active").each(function(idx, li) {
                checkedItems[counter] = $(li).text();
                counter++;
            });
            //$('#display-json').html(JSON.stringify(checkedItems, null, '\t'));
            var checkedItemsArr = Object.keys(checkedItems).map(function (key) { return checkedItems[key]; });
            for (i = 0; i< checkedItemsArr.length;i++) {
                var checkedItem = checkedItemsArr[i];
                api.deleteCompetence(checkedItem, function(a,b,c) {
                    window.location.reload(false);
                });
            }
        });
    });
});
