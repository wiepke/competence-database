/**
 * Created by dehne on 20.10.2016.
 */
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

    $('#competenceCreateButton').click(function () {
        var value1 = $("#verbInput").val();
        var value2 = $("#verbInput2").val();
        var competenceString = "Die S. " + value1 + " " + $("#detailsInput") + " " + value2;
        var operator = value2 + value1;
        var catchwords = $('#tagsInput').tagsinput('items')
        // api call
        if (!Boolean(value1) || !Boolean(value2) || !Boolean(competenceString) || !Boolean(operator) || catchwords.length == 0) {
            $('#createErrorMessage').show();
        } else {
            $('#createErrorMessage').hide();
            $('#createSuccessMessage').show();
        }
        writeCompetenceInDB(competenceString, operator, catchwords);
    });


    //

});


