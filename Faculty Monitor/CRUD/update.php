<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../includes/dbConfig.php');
    if(isset($_POST['submit-data']))
    {
        $from = $_POST['from'];
        $token = $_POST['token'];
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
            'Name'=> $name,
            'Rank' => $rank,
            'Schedule' => $scheduleStart.' - '.$scheduleEnd,
            'Room' => $room,
            'Subject' => $subject,
            'Section' => $section,
            'Attendance' => $attendance,
            'ClassMode' => $classMode,
            'month'=> $month,
            'day' => $day,
            'year' => $year
        ];
        $ref = "Attendance/$token";
        $database = new Database();
        $postData = false;
        if($database->getRecords($ref))//if record is there
        {
            $postData = $database->updateRecord($ref, $data);
        }

        if($postData)
            echo "<script type='text/javascript'>
            var text = 'Record Updated';
            </script> ";
        else
            echo "<script type='text/javascript'>
            var text = 'There was An Error';
            </script> ";
    }
?>
<script type='text/javascript'>
    alert(text);
    if("<?php echo $from?>" == 'calendar')
        window.location.replace('../Display/Attendance/attendancePage.php');
    else 
        window.location.replace('../Display/Tables.php');
    
</script>