/**
 * Created by dehne on 20.10.2016.
 */

$(document).ready(function () {
    // auszuführender Code, nachdem DOM geladen wurden
    $("#test").click(function () {
        alert("hello Julian");
    });


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


    $("#detailsInput").focus(function () {
        var value = $("#verbInput").val();
        if (value.includes(" ")) {
            var value1 = value.split(" ")[0];
            $("#verbInput").val(value1);
            var value2 = value.split(" ")[1];
            $("#verbInput2").val(value2);
        }
    })

    $('#tagsInput').tagsinput({
        freeInput: true
    });

    $('#tagsInput2').tagsinput({
        freeInput: true
    });

    $('#competenceCreateButton').click(function () {
        var value1 = $("#verbInput").val();
        var value2 = $("#verbInput2").val();
        var competenceString = "Die Schüler/Studierenden " + value1 + " " + $("#detailsInput") + " " + value2;
        var operator = value2 + value1;
        var catchwords = $('#tagsInput').val();
        alert(catchwords);
    });

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


