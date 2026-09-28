<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include ('../../includes/dbConfig.php');

if (isset($_POST['submit-data'])) {

    /*******************************************/
    $imageName = '';
    $uploadSuccess = false;

    if (isset($_FILES['photo']) && $_FILES['photo']['error'] === 0) {

        $fileTmpPath = $_FILES['photo']['tmp_name'];
        $fileName = basename($_FILES['photo']['name']);
        $fileExtension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        $allowedTypes = ['jpg', 'jpeg', 'png', 'gif'];

        if (in_array($fileExtension, $allowedTypes)) {

            $cloud_name = 'dvjarguak';
            $api_key    = '655537911836319';
            $api_secret = '5AOq5A3TqDBbOgBROXIc-YQanH4';

            $timestamp = time();
            $string_to_sign = "timestamp={$timestamp}{$api_secret}";
            $signature = sha1($string_to_sign);

            $postData = [
                'file' => new CURLFile($fileTmpPath),
                'api_key' => $api_key,
                'timestamp' => $timestamp,
                'signature' => $signature,
                // Optional:
                // 'folder' => 'attendance_uploads',
                // 'public_id' => pathinfo($fileName, PATHINFO_FILENAME),
                // 'overwrite' => true,
            ];

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
    $token = $_POST['token'];
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

    $data = [
        'scheduleID' => $token,
        'link' => $link,
        'date' => $date,
        'attendance' => $attendance,
        'dressCode' => $dressCode,
        'remarks' => $remarks,
        'imageName' => $imageName
    ];

    if (empty($daysOfweek)) {
        echo "<script>var text = 'Field Days of the Week is Empty';</script>";
    } elseif (strtotime($scheduleStart) >= strtotime($scheduleEnd)) {
        echo "<script>var text = 'Start of Schedule cannot be later, or less than the End of Schedule';</script>";
    } elseif (!$uploadSuccess) {
        echo "<script>var text = 'There was an error with uploading the picture';</script>";
    } else {

        $database = new Database();
        $counters = $database->getRecords('Counter');
        $ref = 'Attendance/Attendance' . $count;
        $postData = false;
        if($counters != null && (!$database->getRecords($ref)))
        {
            $count = $counters['Attendance']['index'];
            $postData = $database->setRecord($ref, $data);
            $count++;
            $countData = ['index' => $count];
        }
        

        if ($postData) {
            echo "<script>var text = 'Record Added Successfully';</script>";
            $database->updateRecord('Counter/Attendance', $countData);
        } else {
            echo "<script>var text = 'There was An Error';</script>";
        }
    }
}
?>

<script>
    alert(text);
    const from = '<?php echo $from; ?>';
    if (from === 'month')
        window.location.href = '../../Display/Attendance/attendancePage.php';
    else if (from === 'week')
        window.location.href = '../../Display/Attendance/weeklyAttendancePage.php';
    else
        window.location.href = '../../Display/Attendance/attendancePage.php';
</script>
