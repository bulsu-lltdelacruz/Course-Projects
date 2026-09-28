<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log In</title>
    <style>
        :root{
        --bg:#f7f9fc; --card:#ffffff; --accent:#6a5af9; --muted:#6b7280; --today:#fde68a;
        }
        * {
        -webkit-print-color-adjust: exact;
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        font-family: 'Poppins', sans-serif;
        }
        .log-in-div{
            display: flex;
            align-items: center;
            justify-content: center;
            justify-self: center;
            width: 20%;
            height: 100vh;
            padding-bottom: 60px;
        }
        .log-in-div form{
            display: flex;
            flex-grow: 1;
            flex-direction: column;
        }
        .log-in-div form {
            display: flex;
            flex-direction: column;
        }
        .log-in-div form .form-group:last-child {
            margin-top: 10px;
        }
    </style>
    <link rel="stylesheet" href="Display/form.css">
<body>
    <?php session_start();
    if (isset($_SESSION['username'])) {
        header('Location: http://localhost/Faculty%20Monitor/Display/Attendance/attendancePage.php');
        exit();
    }
    ?>
    
    <div id="form-schedule" class="log-in-div">
        <form method="post" action="userAuth.php">
            <h1>Log In</h1>
            <div class="form-group">
                <label for="username">Username</label>
                <input required name="username" type="text">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input required name="password" type="password">
            </div>
            <div class="form-group">
                <button class="submit-btn" id="submit-form" name="log-in-btn" type="submit">Log In</button>
            </div>
        </form>
    </div>
</body>
<?php
?>
</html>
    
