<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include '../../includes/dbConfig.php';

$ref = 'Schedule';
$database = new Database();
$scheduleRecords = $database->getRecords($ref);

$tag = $_GET['tag'];
$from = $_GET['from'];
$database = new Database();
$ref ='Faculty/'.$tag;
$postData = $database->remove($ref);
$facultyPostData;

foreach($scheduleRecords as $key=>$value)
{
    if($value['facultyID'] == $tag)
    {
        $facultyPostData= $database->remove("Schedule/$key");
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
        window.location.replace('../../Display/Faculty/facultyList.php');
    // window.location.replace('../Display/Tables.php');
</script>