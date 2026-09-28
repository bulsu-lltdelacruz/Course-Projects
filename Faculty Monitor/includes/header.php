<?php
session_start();
    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    
?>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
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
        html {
          overflow-y: auto;
          scrollbar-gutter: stable;
        }
        body{
            background-color: white;
        }
        h2{
          font-size:26px;
          font-weight:700
        }
        .parent{
        display: flex;
        flex-direction: row;
        /*height: 100vh;*/
        width: 100%;
        background-color: #f4f4f4;
        }

        .side-nav{
        width: 250px;
        background-color: #f4f4f4;
        box-sizing: border-box;
        }
        .container{
        flex: 1;
        width: 100%;
        background-color: #fff;
        box-sizing: border-box;
        overflow-y: auto;
        }
    </style>
    <style>/*sidebar*/
    .sidebar {
      position: fixed;
      width: 250px;
      height: 100vh;
      background: #fff;
      border-radius: 15px;
      padding: 20px 15px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    .profile {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 25px;
    }

    .profile .logo {
      background: #6a5af9;
      color: #fff;
      font-weight: 600;
      padding: 10px;
      border-radius: 10px;
      font-size: 18px;
    }

    .profile-info h2 {
      font-size: 16px;
      color: #222;
    }

    .profile-info p {
      font-size: 12px;
      color: #888;
    }

    .search {
      display: flex;
      align-items: center;
      background: #f6f6fb;
      padding: 8px 10px;
      border-radius: 10px;
      margin-bottom: 15px;
    }

    .search input {
      border: none;
      outline: none;
      background: transparent;
      flex: 1;
      padding-left: 5px;
      color: #555;
    }

    ul {
      list-style: none;
    }

    ul li {
      display: flex;
      align-items: center;
      padding: 10px;
      border-radius: 10px;
      margin-bottom: 8px;
      color: #555;
      font-weight: 500;
      cursor: pointer;
      transition: 0.3s;
    }

    ul li.active {
      background: #6a5af9;
      color: #fff;
    }

    ul li:hover {
      background: #f2f1ff;
    }
    ul li.active:hover {
      background: #6a5af9;
      color: #fff;
    }

    .bottom {
      border-top: 1px solid #eee;
      padding-top: 15px;
    }

    .dark-mode {
      background: #f6f6fb;
      padding: 10px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-top: 15px;
    }

    .toggle {
      width: 35px;
      height: 18px;
      background: #ddd;
      border-radius: 20px;
      position: relative;
      cursor: pointer;
    }

    .toggle::before {
      content: '';
      position: absolute;
      top: 2px;
      left: 2px;
      width: 14px;
      height: 14px;
      background: #fff;
      border-radius: 50%;
      transition: 0.3s;
    }

    .toggle.active {
      background: #6a5af9;
    }

    .toggle.active::before {
      left: 19px;
    }
    .dim{
      position: fixed;        /* stays in place even when scrolling */
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);  /* black with 50% opacity */
      z-index: 900;          /* make sure it's above other elements */
      display: none;
    }
    #log-out button{
      border: none;
      background-color: transparent;
    }
  </style>
</head>
<?php
if (!isset($_SESSION['username'])) {

    header('Location: http://localhost/Faculty%20Monitor/');
    exit();
}
?>
<body>
<div id="dim" class="dim"></div>
<div class="parent">
    <div class="side-nav">
        <div class="sidebar">
            <div>
                <div class="profile">
                    <div class="logo">CL</div>
                    <div class="profile-info">
                        <h2>Faculty Monitor</h2>
                        <p><?php $usernamee = $_SESSION['username'];
                        if(count_chars($usernamee)>20)
                          $usernamee = substr($usernamee, 0,20);
                        echo $usernamee;?></p>
                    </div>
                </div>


                <ul>
                    <li id="home"><span></span>&nbsp; Home</li>
                    <li id="faculty-list"><span></span>&nbsp; Faculty List</li>
                    <li id="schedule" class="active"><span></span>&nbsp; Schedule</li>
                    <li id="attendance"><span></span>&nbsp; Attendance</li>
                    <li onclick="openReports()" id="reports"><span></span>&nbsp; Reports</li>
                    <li id="user"><span></span>&nbsp; User</li>
                </ul>
            </div>

            <div class="bottom">
                <ul>
                  <form id="form-log-out" method="post" action="">
                    <li id="attendance" onclick="submitForm()"><span></span>&nbsp; Log Out</li>
                    <input style="display: none;" type="text" name="log-out" id="" value="">
                    <!--<button type="submit" name="log-out">&nbsp; Log Out</button>-->
                  </form>
                    
                </ul>
            </div>
        </div>
    </div>
    <?php
      if(isset($_POST['log-out']))
      {
        unset($_SESSION['username']);
        echo "<script>window.location.reload();</script>";
      }
    ?>
    <script>
      function submitForm()
      {
        if(confirm("Are you sure you want to Log Out?"))
        {
          document.getElementById('form-log-out').submit();
        }
      }
      function openReports()
      {
        window.location.href = '../Reports/reports.php';
      }
    </script>


    <!--</div>-->