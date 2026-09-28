<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include '../includes/dbConfig.php';

$tag = $_GET['tag'];
$from = $_GET['from'];
$database = new Database();
$ref ='Attendance/'.$tag;
$postData = $database->remove($ref);

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
    if("<?php echo $from?>" == 'calendar')
        window.location.replace('../Display/Attendance/attendancePage.php');
    else {}
       // window.location.replace('../Display/Tables.php');
</script>