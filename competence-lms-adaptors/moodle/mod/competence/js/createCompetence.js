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

});


