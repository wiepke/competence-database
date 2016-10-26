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
    });
});
