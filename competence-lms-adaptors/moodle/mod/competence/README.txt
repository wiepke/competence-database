The following steps should get you up and running with
this module template code.

* DO NOT PANIC!

* deploy the competence database as specified in the README of https://github.com/uzuzjmd/Wissensmodellierung

* Place the widget folder into the /mod folder of the moodle
  directory.

* Change theme to clean

* Go to Site administration -> Plugins -> Web services / Mobile and check the "enable web services for mobile devices" box

* Go to Site Administration > Development > XMLDB editor
  and modify the module's tables.
    -> click on Load
    -> click on Edit
    -> click on new table
  Make sure, that the web server has write-access to the db/ folder.
  You need at least one table, even if your module doesn't use it.

* Visit Site Administration > Notifications, you should find
  the module's tables successfully created

* Go to Site Administration > Plugins > Activity modules > Manage activities
  and you should find that "competence" or Kompetenzmodul has been added to the list of
  installed modules.

* change 
    var serverUrl = "http://localhost:8084";
    var evidenceServerUrl = "http://localhost:8083";
in view.php to your server url (where the database part is deployed)

Good luck!
