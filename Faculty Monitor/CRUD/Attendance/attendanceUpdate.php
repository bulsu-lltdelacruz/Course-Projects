<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../../includes/dbConfig.php');
if(isset($_POST['submit-data']))
{
    /*******************************************/
    $imageName = '';
    $uploadSuccess = false;
    $noImage = false;

    if (!isset($_FILES['photo']) || $_FILES['photo']['error'] === UPLOAD_ERR_NO_FILE) {
        $noImage = true;
    } else if (isset($_FILES['photo']) && $_FILES['photo']['error'] === 0) {
        $fileTmpPath = $_FILES['photo']['tmp_name'];
        $fileName = basename($_FILES['photo']['name']);
        $fileExtension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        $allowedTypes = ['jpg', 'jpeg', 'png', 'gif'];

        if (in_array($fileExtension, $allowedTypes)) {

            // ✅ Cloudinary configuration
            $cloud_name = 'dvjarguak';
            $api_key    = '655537911836319';
            $api_secret = '5AOq5A3TqDBbOgBROXIc-YQanH4';

            // ✅ Folder for upload (optional)
            $timestamp = time();
            $string_to_sign = "timestamp={$timestamp}{$api_secret}";
            $signature = sha1($string_to_sign);

            // ✅ Prepare upload data
            $postData = [
                'file' => new CURLFile($fileTmpPath),
                'api_key' => $api_key,
                'timestamp' => $timestamp,
                'signature' => $signature,
                //'folder' => $folder,
                //'public_id' => pathinfo($fileName, PATHINFO_FILENAME),
                //'overwrite' => true,
            ];

            // ✅ Upload to Cloudinary via cURL
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "https://api.cloudinary.com/v1_1/$cloud_name/image/upload");
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
            $response = curl_exec($ch);
            $error = curl_error($ch);
            curl_close($ch);

            if ($error) {
                echo "<script>alert('Cloudinary Upload Error: " . addslashes($error) . "');</script>";
            } else {
                $result = json_decode($response, true);
                if (isset($result['secure_url'])) {
                    $imageName = $result['secure_url'];
                    $uploadSuccess = true;
                } else {
                    echo "<script>alert('Upload failed: " . addslashes($response) . "');</script>";
                }
            }

        } else {
            echo "<script>alert('Only JPG, JPEG, PNG, and GIF files are allowed.');</script>";
        }
    } else {
        echo "<script>alert('No file uploaded or there was an upload error.');</script>";
    }
    /*******************************************/

    $from = $_POST['from'];
    //$from = $_POST['from-edit'];
    $token = $_POST['token-attendance'];
    $section = $_POST['section'];
    $subject = $_POST['subject'];
    $room = $_POST['room'];
    $classMode = $_POST['class-mode'];
    $daysOfweek = $_POST['days-of-week'];
    $scheduleStart = $_POST['schedule-start'];
    $scheduleEnd = $_POST['schedule-end'];
    $link = $_POST['link'];
    $date = $_POST['date'];
    $attendance = $_POST['attendance-status'];
    $dressCode = $_POST['dress-code'];
    $remarks = $_POST['remarks'];
    //$monthFrom = $_POST['month-from'];
    //$monthTo = $_POST['month-to'];
    $scheduleStart = $_POST['schedule-start'];
    $scheduleEnd = $_POST['schedule-end'];
    $data;

    if($noImage)
    {
        $data = [
            'link' => $link,
            'date' => $date,
            'attendance' => $attendance,
            'dressCode' => $dressCode,
            'remarks' => $remarks
        ];
    }
    else
    {
        $data = [
            'link' => $link,
            'date' => $date,
            'attendance' => $attendance,
            'dressCode' => $dressCode,
            'remarks' => $remarks,
            'imageName' => $imageName
        ];
    }
    
     echo "<script type='text/javascript'>
        console.log('$token');
        </script> ";

    if(empty($daysOfweek) || $daysOfweek === null || $daysOfweek === '' || strlen( $daysOfweek) <=0)
    {
         echo "<script type='text/javascript'>
        var text = 'Field Days of the Week is Empty';
        </script> ";
    }
    else if(strtotime($scheduleStart)>= strtotime($scheduleEnd))
    {
        echo "<script type='text/javascript'>
        var text = 'Start of Schedule cannot be later, or less than the End of Schedule';
        </script> ";
    }
    else if(!$uploadSuccess && !$noImage)
    {
        echo "<script type='text/javascript'>
        var text = 'There was an error with uploading the picture';
        </script> ";
    }
    else
    {
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
}
?>
<script>
    alert(text);
    from = '<?php echo $from;?>';
    console.log(from);
    
    if(from == 'month')
        window.location.href = '../../Display/Attendance/attendancePage.php';
    else if(from == 'week')
        window.location.href = '../../Display/Attendance/weeklyAttendancePage.php';
    else if(from == 'table')
        window.location.href = '../../Display/Attendance/attendanceTable.php';
    else
        window.location.href = '../../Display/Attendance/attendancePage.php';
</script>
