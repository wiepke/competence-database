
var modulesSelected = {};

$(document).ready(function () {

    $('#createErrorMessage').hide();
    $('#createSuccessMessage').hide();


    $("#detailsInput").focus(function() {
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
            $('#activityList').append("<li class=\"list-group-item\" id=\"activity"+i+"\">" + label + "</li>");
        }
    }

    if (courseModules.length == 0) {
        $('#activityList').append("<li class=\"list-group-item\" id=\"activity"+i+"\">" + "Es wurden noch keine Aktivitäten angelegt" + "</li>");
    }

    for (i = 0; i < courseModules.length; i++) {
        if (courseModules[i].modname != "competence") {
            $('#activity'+i).click(function(){
                var indexClicked = this.id.replace("activity","");
                modulesSelected[indexClicked] = !modulesSelected[indexClicked];
                console.log(modulesSelected);
            });
        }
    }

    $('#defaultQuestionsAdder').click(function(){
        $('#questionsArea').text(
            "Wie motiviert bist du das Lernziel zu erreichen?,Warum möchtest du das Lernziel erreichen?,Welches Vorwissen hast du, welches dir zum Erreichen des Ziels dient?,Was wird ein Ergebnis deines Lernens sein?,Wie möchtest du das Lernziel erreichen?,Wen kannst du Fragen, wenn du nicht weiterkommst?,Kannst du einfach im Internet suchen um weiterzukommen?,Findest du, dass das Lernziel zu einfach ist?"
        );
    });

    //$('#activityList').append("<li class=\"list-group-item\">"+courseModules.length+"</li>");

});


