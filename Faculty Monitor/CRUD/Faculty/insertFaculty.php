 <?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../../includes/dbConfig.php');
    if(isset($_POST['submit-data']))
    {
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
        $ref = 'Faculty';
        $database = new Database();
        $facultyRecords = $database->getRecords($ref);
        $duplicate = false;
        if($facultyRecords != null)
        foreach($facultyRecords as $key => $value){
            if($firstName ==  $value['first-name']
            && $middleName == $value['middle-name']
            && $lastName == $value['last-name'])
            {
                $duplicate = true;
            }
            if($idNum ==  $value['id-num'])
            {
                $duplicate = true;
            }
        }
        if($duplicate)
        {
             echo "<script type='text/javascript'>
            var text = 'Duplicate Entry';
            </script> ";
        }
        else
        {
            $database = new Database();
            $counters = $database->getRecords('Counter');
            
            $count =  $counters['Faculty']['index'];
            
            $ref = 'Faculty/Faculty'.$count;
            $postData = $database->setRecord($ref, $data);
            $count++;
            $countData = ['index'=>$count];

            if($postData)
            {
                echo "<script type='text/javascript'>
                var text = 'Record Added';
                </script> ";
                $database->updateRecord('Counter/Faculty', $countData);
            }
            else
                echo "<script type='text/javascript'>
                var text = 'There was An Error';
                </script> ";
                
        }
        
        
    }
?>
<script type='text/javascript'>
    alert(text);
    //history.back();
    window.location.href = '../../Display/Faculty/facultyList.php';
</script>
