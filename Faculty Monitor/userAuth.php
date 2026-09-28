<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include 'includes/dbConfig.php';

if (isset($_POST['log-in-btn'])) {
    session_start();
    $apiKey = "AIzaSyAHmpLIptsjgLCLEH2I9A9UW-kbzv_Nw7Q";

    $url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" . $apiKey;
    $username = $_POST['username'];
    $password = $_POST['password'];
    $data = [
        "email" => $username,
        "password" => $password,
        "returnSecureToken" => true
    ];

    $options = [
        "http" => [
            "header"  => "Content-Type: application/json\r\n",
            "method"  => "POST",
            "content" => json_encode($data)
        ]
    ];

    $context = stream_context_create($options);
    $response = file_get_contents($url, false, $context);

    if ($response === FALSE) {
        echo "<script>
            alert('Log In Unsuccessful');
            window.location.href = 'index.php';
        </script>";
    } else {
        $responseData = json_decode($response, true);
        $uid = $responseData['localId'];

        $ref = 'Account/' . $uid;
        $database = new Database();
        $records = $database->getRecords($ref);
        $role = $records['role'] ?? ''; // prevent undefined index warning

        if ($role === 'user') {
            echo "<script>
                alert('User cannot use this system');
                window.location.href = 'index.php';
            </script>";
        } else {
            echo "<script>alert('Log In Successful');</script>";
            $_SESSION['username'] = $username;
            $_SESSION['uid'] = $uid;
            echo "<script>window.location.href = 'Display/Attendance/attendancePage.php';</script>";
        }
    }
}
?>
