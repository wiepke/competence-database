/**
 * Created by dehne on 26.10.2016.
 */
define(['jquery','node_modules/superagent/superagent', 'index'], function ($,superagent,main) {
    return {
        initialise: function () {
            // set path for superagent

            console.log("got here")
            var apiClient = new main.ApiClient();
            apiClient.basePath = 'http://fleckenroller.cs.uni-potsdam.de/app/competence-base';
            //apiClient.basePath = 'http://localhost:8080/competence-base';
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
        }
    };
});