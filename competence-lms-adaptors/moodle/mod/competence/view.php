
<?php
require_once(dirname(dirname(dirname(__FILE__))) . '/config.php');
require_once(dirname(__FILE__) . '/lib.php');


// ............................... the header and includes ...................................................
?>
<script type='text/javascript' src='js/lib/jquery3.1.1.js'> </script>
<link rel="stylesheet" href="//cdn.jsdelivr.net/bootstrap.tagsinput/0.4.2/bootstrap-tagsinput.css" />
<script src="//cdn.jsdelivr.net/bootstrap.tagsinput/0.4.2/bootstrap-tagsinput.min.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<link rel="stylesheet" type="text/css" href="css/competenceCreation.css"/>

<script type='text/javascript' src='js/lib/typeahead.js'> </script>
<script type='text/javascript' src='js/config.js'> </script>
<script type='text/javascript' src='js/createCompetence.js'> </script>
<script type='text/javascript' src='js/UIFunctions.js'> </script>
<script type='text/javascript' src='js/typeaheads.js'> </script>
<script type='text/javascript' src='js/ajax.js'> </script>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<?php
// ............................... END the header and includes ...................................................

// weird settings part 1

$id = optional_param('id', 0, PARAM_INT); // course_module ID, or
$n = optional_param('n', 0, PARAM_INT);  // competence instance ID - it should be named as the first character of the module

if ($id) {
    $cm = get_coursemodule_from_id('competence', $id, 0, false, MUST_EXIST);
    $course = $DB->get_record('course', array('id' => $cm->course), '*', MUST_EXIST);
    $competence = $DB->get_record('competence', array('id' => $cm->instance), '*', MUST_EXIST);
} elseif ($n) {
    $competence = $DB->get_record('competence', array('id' => $n), '*', MUST_EXIST);
    $course = $DB->get_record('course', array('id' => $competence->course), '*', MUST_EXIST);
    $cm = get_coursemodule_from_instance('competence', $competence->id, $course->id, false, MUST_EXIST);
} else {
    error('You must specify a course_module ID or an instance ID');
}

require_login($course, true, $cm);
$context = context_module::instance($cm->id);
add_to_log($course->id, 'competence', 'view', "view.php?id={$cm->id}", $competence->name, $cm->id);



// INTERESTING DATA FOR THE APP .............................................................

$roleString = "'student'";
if (user_has_role_assignment($USER->id, 3)) {
       $roleString = "'teacher'";
}

$courseId =  "'".$id."'";
$courseName =  "'".$COURSE->fullname."'";

// TODO get activitäten

// and writing it to javascript
?>
<script type="text/javascript">
var role = <?php echo $roleString; ?>;
var courseId = <?php echo $courseId; ?>;
var courseName = <?php echo $courseName; ?>;

</script>
<?php

// INTERESTING DATA FOR THE APP ..............................................................



// .......... weird settings  part 2............................................



/// Print the page header
$PAGE->set_url('/mod/competence/view.php', array('id' => $cm->id));
$PAGE->set_title(format_string($competence->name));
$PAGE->set_heading(format_string($course->fullname));
$PAGE->set_context($context);
echo $OUTPUT->header();
if ($competence->intro) { // Conditions to show the intro can change to look for own settings or whatever
    echo $OUTPUT->box(format_module_intro('competence', $competence, $cm->id), 'generalbox mod_introbox', 'competenceintro');
}
// .......... END weird settings............................................
?>


<!-- ............................... the HTML ............................................-->

<h1>Kompetenzen aus der COMPBASE</h1>
<div id="response"></div>
<h1>Kompetenz wurde erstellt?</h1>
<div id="response2"></div>


<h1>Lernziele verwalten</h1>


<div class="panel panel-default outerPanel" id="outerPanel1">

<h2>Ein Lernziel anlegen</h2>

<div id="competenceCreation">
  <span>Die Schüler/Studierenden</span>
  <input id="verbInput" class="typeahead" type="text" placeholder="können....">
  <textarea id="detailsInput" rows="3" placeholder="Lerngegenstand..."></textarea>
  <input type="text"  id="verbInput2">.</input>
  <h4>Tags<h4>
  <input type="text" id="tagsInput" placeholder="Schlagwort1...."></input>
  <h4>Vorgeschlagene Aktivitäten</h4>
  <ul class="list-group checked-list-box" id="activityList">
      <li class="list-group-item">Cras justo odio</li>
      <li class="list-group-item" data-checked="true">Dapibus ac facilisis in</li>
                    <li class="list-group-item">Morbi leo risus</li>
                    <li class="list-group-item">Porta ac consectetur ac</li>
                    <li class="list-group-item">Vestibulum at eros</li>
                    <li class="list-group-item">Cras justo odio</li>
  </ul>

   <h4>Vorgeschlagene Badges</h4>
    <ul class="list-group checked-list-box" id="badgesList">
        <li class="list-group-item">Cras justo odio</li>
        <li class="list-group-item" data-checked="true">Dapibus ac facilisis in</li>
    </ul>

    <h4>Reflexionsfragen</h4>
    <input id="tagsInput" rows="10" placeholder="Glaubst du, du hast alle Voraussetzungen für dieses Lernziel erfüllt?..."></input>
    </br>
    <label for="defaultQuestions">Default Fragen hinzufügen?</label>
    <input id="defaultQuestions" type="checkbox" checked>
    </br>
    </br>
  <button id="competenceCreateButton" type="button" class="btn btn-primary">anlegen</button>
</div>

<div id="createSuccessMessage" class="alert alert-success" role="alert">
              <strong>Sehr gut!</strong> Das Lernziel wurde angelegt.
</div>

<div id="createErrorMessage" class="alert alert-error" role="alert">
        Es wurden nicht alle Felder ausgefüllt.
</div>

</div>

<div class="panel panel-default outerPanel" id="outerPanel2">
    <h2>Bestehende Lernziele bearbeiten</h2>
        <ul class="list-group checked-list-box" id="competenceList">
          <li class="list-group-item">Cras justo odio</li>
          <li class="list-group-item" data-checked="true">Dapibus ac facilisis in</li>
                        <li class="list-group-item">Morbi leo risus</li>
                        <li class="list-group-item">Porta ac consectetur ac</li>
                        <li class="list-group-item">Vestibulum at eros</li>
                        <li class="list-group-item">Cras justo odio</li>
        </ul>
        <br>
        <button id="competenceDeleteButton" type="button" class="btn btn-primary">löschen</button>
</div>
<!-- the actual implementation start-->






<!-- the actual implementation finish -->
<?php

// ............................... END the HTML ............................................
echo $OUTPUT->footer();
