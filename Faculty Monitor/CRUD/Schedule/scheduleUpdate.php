<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../../includes/dbConfig.php');
    if(isset($_POST['submit-data']))
    {
        $from = $_POST['from-edit'];
        $token = $_POST['token'];
        $faculty = $_POST['name'];
        $section = $_POST['section'];
        $subject = $_POST['subject'];
        $room = $_POST['room'];
        $classMode = $_POST['class-mode'];
        $link = $_POST['link'];
        $daysOfweek = $_POST['days-of-week'];
        $monthFrom = $_POST['month-from'];
        $monthTo = $_POST['month-to'];
        $scheduleStart = $_POST['schedule-start'];
        $scheduleEnd = $_POST['schedule-end'];
        $data = [
            'section' => $section,
            'subject' => $subject,
            'room'=> $room,
            'classMode'=> $classMode,
            'link' => $link,
            'daysOfWeek' => $daysOfweek,
            'monthFrom' => $monthFrom,
            'monthTo' => $monthTo,
            'scheduleStart' => $scheduleStart,
            'scheduleEnd' => $scheduleEnd,
            'facultyID' => $faculty
        ];
        $monthFromNum = explode('-',$monthFrom)[1];
        $monthToNum = explode('-',$monthTo)[1];
        if(empty($daysOfweek) || $daysOfweek === null || $daysOfweek === '' || strlen( $daysOfweek) <=0)
        {
             echo "<script type='text/javascript'>
            var text = 'Field Days of the Week is Empty';
            </script> ";
        }
        else if($monthFromNum >$monthToNum)
        {
             echo "<script type='text/javascript'>
            var text = 'Month To cannot be earlier than Month From';
            </script> ";
        }
        else if(strtotime($scheduleStart)>= strtotime($scheduleEnd))
        {
            echo "<script type='text/javascript'>
            var text = 'Start of Schedule cannot be later, or less than the End of Schedule';
            </script> ";
        }
        else
        {
            $ref = "Schedule/$token";
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
    }
?>
<script>
    alert(text);
    from = '<?php echo $from;?>';
    console.log(from);
    
    //history.back();
    if(from == 'month')
        window.location.href = '../../Display/Schedule/schedule.php';
    else if(from == 'week')
        window.location.href = '../../Display/Schedule/weeklySchedulePage.php';
    else if(from == 'table')
        window.location.href = '../../Display/Schedule/scheduleTable.php';
    else
        window.location.href = '../../Display/Schedule/schedule.php';
    
</script>