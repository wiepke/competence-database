
<?php
require_once(dirname(dirname(dirname(__FILE__))) . '/config.php');
require_once(dirname(__FILE__) . '/lib.php');


// ............................... the header and includes ...................................................
?>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.2/require.js"></script>

<link rel="stylesheet" href="//cdn.jsdelivr.net/bootstrap.tagsinput/0.4.2/bootstrap-tagsinput.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="css/competenceCreation.css"/>

<script type='text/javascript' src='js/config.js'> </script>
<script type='text/javascript' src='js/ajax.js'> </script>
<script type='text/javascript' src="js/lib/tagsinput.js"></script>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<?php
// ............................... END the header and includes ...................................................

// weird settings part 1

$id = optional_param('id', 0, PARAM_INT); // course_module ID, or
$n = optional_param('n', 0, PARAM_INT);  // competence instance ID - it should be named as the first character of the module
$cm = null;

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

//$modinfo = mod_frog_get_coursemodule_info($cm);

require_login($course, true, $cm);
$context = context_module::instance($cm->id);
add_to_log($course->id, 'competence', 'view', "view.php?id={$cm->id}", $competence->name, $cm->id);



// INTERESTING DATA FOR THE APP .............................................................



$courseId =  "'".$id."'";
$courseName =  "'".$COURSE->fullname."'";

// get the activities
$query = 'SELECT {log}.*,firstname,lastname,username,email,lastaccess FROM {log} , {user} INNER JOIN {role_assignments} ra ON ra.userid = {user}.id INNER JOIN {context} ct ON ct.id = ra.contextid INNER JOIN {course} c ON c.id = ct.instanceid INNER JOIN {role} r ON r.id = ra.roleid INNER JOIN {course_categories} cc ON cc.id = c.category WHERE {log}.userid = {user}.id AND {log}.course= ? ORDER BY time DESC';
$result = $DB->get_records_sql($query, array($courseId));
  $mapper = function ($arrayElement) {
      return array(
           'url' => "/mod/" . $arrayElement->module . "/" . $arrayElement->url,
           "username" => $arrayElement->username,
           'changed' => $arrayElement->lastaccess,
           'activityTyp' => $arrayElement->module,
           'email' => $arrayElement->email
      );
   };
$result_mapped = array_map($mapper, $result);
$activities = "'".json_encode($result_mapped)."'";

// get the modules
$modinfo = get_fast_modinfo($COURSE->id);
$infoMapper = function ($arrayElement) {
      return array(
           "url" => "/mod/".$arrayElement->modname."/view.php?id=".$arrayElement->id,
           'name' => $arrayElement->name,
           "modname" => $arrayElement->modname
      );
   };
$modinfo_mapped = array_map($infoMapper, $modinfo->cms);
$info = "'".json_encode(array_values($modinfo_mapped))."'";


// get roles

$roleString = "'undefined'";

//$roleString = "'notgiven'";

//if (user_has_role_assignment($USER->id, 3)) {
//       $roleString = "'teacher'";
//       echo $roleString;
//}

if (!$cm = get_coursemodule_from_id('competence', $cm->id)) {
    error("Course module ID was incorrect");
}

if (has_capability('mod/competence:view', $context)) {
       $roleString = "'teacher'";
}



// TODO get activitäten

// and writing it to javascript
?>
<script type="text/javascript">
var role = <?php echo $roleString; ?>;
var courseId = <?php echo $courseId; ?>;
var courseName = <?php echo $courseName; ?>;
var activities = <?php echo $activities; ?>;
var courseModules = JSON.parse(<?php echo $info; ?>);

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


// role management





?>


<!-- ............................... the HTML ............................................-->

<div id="competence-management-view">

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
  </ul>
    <!--
   <h4>Vorgeschlagene Badges</h4>
    <ul class="list-group checked-list-box" id="badgesList">
        <li class="list-group-item">Cras justo odio</li>
        <li class="list-group-item" data-checked="true">Dapibus ac facilisis in</li>
    </ul>
    -->
    <h4>Reflexionsfragen</h4>
    <textarea id="questionsArea"  rows="10" placeholder="Frage1; Frage2...."></textarea>
    </br>
    <button id="defaultQuestionsAdder" type="button" class="btn btn-secondary">Default Fragen hinzufügen</button>
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

        </ul>
        <br>
        <button id="competenceDeleteButton" type="button" class="btn btn-primary">löschen</button>
</div>
<!-- the actual implementation start-->

</div>

<div id="roleErrorMessage" class="alert alert-error" role="alert">
        Sie müssen eine Lehrenden-Rolle haben, um diese Ansicht zu öffnen.
</div>



<!-- the actual implementation finish -->


<?php


// ............................... END the HTML ............................................
echo $OUTPUT->footer();
