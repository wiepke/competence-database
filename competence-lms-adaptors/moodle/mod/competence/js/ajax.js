/**
 * Created by dehne on 24.10.2016.
 */
var modulesSelected = {};

// Run code
    requirejs.config({
        //By default load any module IDs from js/lib
        baseUrl: '',
        //except, if the module ID starts with "app",
        //load it from the js/app directory. paths
        //config is relative to the baseUrl, and
        //never includes a ".js" extension since
        //the paths config could be for a directory.
        //'bootstrap-tagsinput': ['//cdn.jsdelivr.net/bootstrap.tagsinput/0.4.2/bootstrap-tagsinput.min'],
        paths: {
            'jquery': ['//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min', 'libs/jquery-min'],
            'superagent': 'node_modules/superagent/superagent',
            'bloodhound': 'js/lib/bloodhound',
            'typeahead': 'js/lib/typeahead',
            'tagsinput': 'js/lib/tagsinput',
            'bootstrap': ['//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min', 'libs/bootstrap-min'],
        },
        shim: {
            /* Set bootstrap dependencies (just jQuery) */
            'bootstrap': ['jquery']
        }
    });

    //.............UI STUFF ..........................
    requirejs(['jquery', 'typeahead', 'bloodhound', 'bootstrap', 'tagsinput'], function ($, a, b, c, d) {

        if (!(role == "teacher")) {
            $('#competence-management-view').hide();
        } else {
            $('#roleErrorMessage').hide();
        }

        var substringMatcher = function (strs) {
            return function findMatches(q, cb) {
                var matches, substringRegex;

                // an array that will be populated with substring matches
                matches = [];

                // regex used to determine if a string contains the substring `q`
                substrRegex = new RegExp(q, 'i');

                // iterate through the pool of strings and for any string that
                // contains the substring `q`, add it to the `matches` array
                $.each(strs, function (i, str) {
                    if (substrRegex.test(str)) {
                        matches.push(str);
                    }
                });

                cb(matches);
            };
        };

        var states = ['nennen', 'arbeiten heraus', 'beschreiben', 'charakterisieren', 'erstellen',
            'stellen dar', 'analysieren', 'ordnen zu', 'ordnen ein', 'begründen', 'erklären',
            'erläutern', 'vergleichen', 'überprüfen', 'beurteilen', 'bewerten', 'erörtern', 'gestalten'
        ];

        // constructs the suggestion engine
        var states = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.whitespace,
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            // `states` is an array of state names defined in "The Basics"
            local: states
        });


        $('#competenceCreation .typeahead').typeahead({
                hint: true,
                highlight: true,
                minLength: 1
            },
            {
                name: 'states',
                source: states
            });

        $('#createErrorMessage').hide();
        $('#createSuccessMessage').hide();


        //     $("#verbInput").blur(function () {
        $("#detailsInput").focus(function () {
            var value = $("#verbInput").val();
            if (value.includes(" ")) {
                var value1 = value.split(" ")[0];
                $("#verbInput").val(value1);
                var value2 = value.split(" ")[1];
                $("#verbInput2").val(value2);
            }
        });


        $('#tagsInput').tagsinput({
            freeInput: true
        });

        $('#tagsInput2').tagsinput({
            freeInput: true
        });


        $('#get-checked-data').on('click', function (event) {
            event.preventDefault();
            var checkedItems = {}, counter = 0;
            $("#check-list-box li.active").each(function (idx, li) {
                checkedItems[counter] = $(li).text();
                counter++;
            });
            $('#display-json').html(JSON.stringify(checkedItems, null, '\t'));
        });

        for (i = 0; i < courseModules.length; i++) {
            var label = courseModules[i].name;
            if (courseModules[i].modname != "competence") {
                $('#activityList').append("<li class=\"list-group-item\" id=\"activity" + i + "\">" + label + "</li>");
            }
        }

        if (courseModules.length == 0) {
            $('#activityList').append("<li class=\"list-group-item\" id=\"activity" + i + "\">" + "Es wurden noch keine Aktivitäten angelegt" + "</li>");
        }

        for (i = 0; i < courseModules.length; i++) {
            if (courseModules[i].modname != "competence") {
                $('#activity' + i).click(function () {
                    var indexClicked = this.id.replace("activity", "");
                    modulesSelected[indexClicked] = !modulesSelected[indexClicked];
                    console.log(modulesSelected);
                });
            }
        }

        $('#defaultQuestionsAdder').click(function () {
            $('#questionsArea').text(
                "Wie motiviert bist du das Lernziel zu erreichen?;Warum möchtest du das Lernziel erreichen?;Welches Vorwissen hast du, welches dir zum Erreichen des Ziels dient?;Was wird ein Ergebnis deines Lernens sein?;Wie möchtest du das Lernziel erreichen?;Wen kannst du Fragen, wenn du nicht weiterkommst?;Kannst du einfach im Internet suchen um weiterzukommen?;Findest du, dass das Lernziel zu einfach ist?"
            );
        });
    });


    /*********************** THE AJAX CALLS *********************************/
    requirejs(['jquery', 'index'], function ($, main) {
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
            var competenceString = ("S. " + value1 + " " + $("#detailsInput").val() + " " + value2).trim();
            var operator = value2 + value1;
            var catchwords = $('#tagsInput').tagsinput('items')
            // api call
            if (!Boolean(value1) || !Boolean(competenceString) || !Boolean(operator) || catchwords.length == 0) {
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
                console.log("sending competence" + competenceString);
                // sending the competence
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
                        // add the course to the db
                        api.addCourse(courseId, courseBody, function (a, b, c) {
                            if (c.statusText == "OK") {
                                for (i = 0; i < activitiesSuggested.length; i++) {
                                    var activityToSend = activitiesSuggested[i];
                                    // add course activities
                                    api.addActivity({body: activityToSend}, function (a, b, c) {
                                        for (y = 0; y < activitiesSuggested.length; y++) {
                                            var activityToSend2 = activitiesSuggested[y];
                                            api.addActivity_0(competenceString, activityToSend2, function (a, b, c) {
                                                var questions = $('#questionsArea').text().split(";");
                                                for (x = 0; x < questions.length; x++) {
                                                    var question = {
                                                        "question": questions[x],
                                                        "competenceId": competenceString
                                                    };
                                                    // add reflective questions
                                                    api.addReflectiveQuestionToCompetence({"body": question}, function (a, b, c) {
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
            }
        });

        // lernziele bearbeiten
        api.getCompetences({"courseId": courseId}, function (error, data, response) {
            console.log("getting competences");
            var competences = response.body;
            for (i = 0; i < competences.length; i++) {
                var competencePointed = competences[i];
                $('#competenceList').append("<li class=\"list-group-item\" id=\"competence" + i + "\">" + competencePointed + "</li>");
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
        $('#competenceDeleteButton').on('click', function (event) {
            event.preventDefault();
            var checkedItems = {}, counter = 0;
            $("#competenceList li.active").each(function (idx, li) {
                checkedItems[counter] = $(li).text();
                counter++;
            });
            //$('#display-json').html(JSON.stringify(checkedItems, null, '\t'));
            var checkedItemsArr = Object.keys(checkedItems).map(function (key) {
                return checkedItems[key];
            });
            for (i = 0; i < checkedItemsArr.length; i++) {
                var checkedItem = checkedItemsArr[i];
                api.deleteCompetence(checkedItem, function (a, b, c) {
                    window.location.reload(false);
                });
            }
        });
    });



