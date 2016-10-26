/**
 * Created by dehne on 24.10.2016.
 */
$(document).ready(function () {

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
});