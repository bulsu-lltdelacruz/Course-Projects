<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../includes/dbConfig.php');
    if(isset($_POST['submit-data']))
    {
        $month = $_POST['month'];
        $day = $_POST['day'];
        $year = $_POST['year'];

        $name = $_POST['name'];
        $rank = $_POST['rank'];
        $scheduleStart = $_POST['schedule-start'];
        $scheduleEnd = $_POST['schedule-end'];
        $room = $_POST['room'];
        $subject = $_POST['subject'];
        $section = $_POST['section'];
        $attendance = $_POST['attendance'];
        $classMode = $_POST['class-mode'];
        $data = [
            'month'=> $month,
            'day' => $day,
            'year' => $year,
            'Name'=> $name,
            'Rank' => $rank,
            'Schedule' => $scheduleStart.' - '.$scheduleEnd,
            'Room' => $room,
            'Subject' => $subject,
            'Section' => $section,
            'Attendance' => $attendance,
            'ClassMode' => $classMode
        ];
        //$database->setRecord('Real/index', $tag);
        
        $database = new Database();
        $counters = $database->getRecords('Counter');

        $count =  $counters['Faculty']['index'];

        $ref = 'Attendance/Faculty'.$count;
        $postData = $database->setRecord($ref, $data);

        if($postData)
            echo "<script type='text/javascript'>
            var text = 'Record Added';
            </script> ";
        else
            echo "<script type='text/javascript'>
            var text = 'There was An Error';
            </script> ";
        
    }
?>
<script type='text/javascript'>
    alert(text);
    //history.back();
    window.location.href = '../Display/Attendance/attendancePage.php';
</script>