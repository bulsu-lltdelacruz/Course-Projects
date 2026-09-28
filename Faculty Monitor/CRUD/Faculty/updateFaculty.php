 <?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../../includes/dbConfig.php');
    if(isset($_POST['submit-data']))
    {
        $token = $_POST['token'];
        $idNum = $_POST['id-num'];
        $lastName = $_POST['last-name'];
        $firstName = $_POST['first-name'];
        $middleName = $_POST['middle-name'];
        $gender = $_POST['gender'];
        $rank = $_POST['rank'];
        $facultyStatus = $_POST['faculty-status'];
        $department = $_POST['department'];
        $data = [
            'id-num'=> $idNum,
            'last-name' => $lastName,
            'first-name' => $firstName,
            'middle-name'=> $middleName,
            'gender' => $gender,
            'rank' => $rank,
            'faculty-status' => $facultyStatus,
            'department' => $department
        ];
        //$database->setRecord('Real/index', $tag);
        
        $database = new Database();
        $ref = "Faculty/$token";
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
    //history.back();
    window.location.href = '../../Display/Faculty/facultyList.php';
</script>
