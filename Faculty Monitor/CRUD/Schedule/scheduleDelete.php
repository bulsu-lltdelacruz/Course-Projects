<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include '../../includes/dbConfig.php';

$tag = $_GET['tag'];
$from = $_GET['from'];
$database = new Database();
$ref ="Schedule/$tag";
$postData = $database->remove($ref);
$attendanceRecords = $database->getRecords('Attendance');

foreach($attendanceRecords as $key=>$value)
{
    if($value['scheduleID'] == $tag)
    {
        $facultyPostData= $database->remove("Attendance/$key");
    }
}

if($postData)
    echo "<script type='text/javascript'>
    var text = 'Record Updated';
    </script> ";
else
    echo "<script type='text/javascript'>
    var text = 'There was An Error';
    </script> ";
?>
<script>
    from = '<?php echo $from;?>';
    console.log(from);

    if(from == 'calendar')
        window.location.replace('../../Display/Schedule/schedule.php');
    else if(from == 'week')
        window.location.href = '../../Display/Schedule/weeklySchedulePage.php';
    else if(from == 'table')
        window.location.href = '../../Display/Schedule/scheduleTable.php';
    else
        window.location.href = '../../Display/Schedule/schedule.php';
</script>