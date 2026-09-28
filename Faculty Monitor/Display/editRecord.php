<?php
    include('../includes/header.php');
    include '../includes/dbConfig.php';
    $token = $_GET['tag'];
    $from = $_GET['from'];
    $month = date("F");
    $day =  date("j");
    $year = '2025';
    //echo $tag;
    $ref = 'Attendance/';
    $database = new Database();
    $records = $database->getRecords($ref);
    $editData;
    foreach($records as $tag => $dict)
    {
        if($tag != $token)
            continue;
        //echo 'Edit '.$dict['Name']."'s Record";
        if(array_key_exists('month', $dict) &&
        array_key_exists('day', $dict) &&
        array_key_exists('year', $dict))
        {
            $month = $dict['month'];
            $day = $dict['day'];
            $year = $dict['year'];
        }
            $editData = $dict;
       
        
    }
    
?>
<style>
    .form-group {
        display: flex;
        flex-direction: column;
    }
    .form-group input, .form-group select{
        height: 30px;
    }
    .form-div{
        width: 50%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        margin-left: 50%;
        translate: -50%;
    }
    .form-div form{
        width: 100%;
    }
    .selection-group{
        display: flex;
        flex-direction: row;
    }
    .selection-group select{
        width: 48.5%;
    }
    .selection-group select:last-child {
        margin-left: 3%;
    }
</style>
<div id="update-form" class="form-div">
    <form  action="../CRUD/update.php" method="post">
        <input style="display: none;" type="text" name="month" id="month" value="<?php echo $month;?>">
        <input style="display: none;" type="text" name="day" id="day" value="<?php echo $day;?>">
        <input style="display: none;" type="text" name="year" id="year" value="<?php echo $year;?>">
        <input style="display: none;" type="text" name="from" id="from" value="<?php echo $from;?>">
        
        <input style="display: none;" name="token" value="<?php echo $token;?>"/>
        <h1>Edit <?php echo $editData['Name']?>'s Record</h1>
        <div class="form-group">
            <label for="name">Name</label>
            <input required type="text" name="name" value="<?php echo $editData['Name']?>" class="" placeholder="Name">
        </div>
        <div class="form-group">
            <label for="rank">Rank</label>
            <select required name="rank" id="rank">
                <option selected value="Temporary">Temporary</option>
                <option value="Part-Time">Part-Time</option>
                <option value="Permanent">Permanent</option>
            </select>
            <!--<input type="text" name="rank" class="" placeholder="Rank">-->
        </div>
        <div class="form-group">
            <label for="schedule">Schedule</label>
            <div class="selection-group">
                <select required name="schedule-start" id="schedule-start">
                    <option selected value="7:00 AM">7:00 AM</option>
                    <option selected value="7:30 AM">7:30 AM</option>
                    <option selected value="8:00 AM">8:00 AM</option>
                    <option selected value="8:30 AM">8:30 AM</option>
                    <option selected value="9:00 AM">9:00 AM</option>
                    <option selected value="9:30 AM">9:30 AM</option>
                    <option selected value="10:00 AM">10:00 AM</option>
                    <option selected value="10:30 AM">10:30 AM</option>
                    <option selected value="11:00 AM">11:00 AM</option>
                    <option selected value="11:30 AM">11:30 AM</option>
                    <option selected value="12:00 PM">12:00 PM</option>
                    <option selected value="12:30 PM">12:30 PM</option>
                    <option selected value="1:00 PM">1:00 PM</option>
                    <option selected value="1:30 PM">1:30 PM</option>
                    <option selected value="2:00 PM">2:00 PM</option>
                    <option selected value="2:30 PM">2:30 PM</option>
                    <option selected value="3:00 PM">3:00 PM</option>
                    <option selected value="3:30 PM">3:30 PM</option>
                    <option selected value="4:00 PM">4:00 PM</option>
                    <option selected value="4:30 PM">4:30 PM</option>
                    <option selected value="5:00 PM">5:00 PM</option>
                    <option selected value="5:30 PM">5:30 PM</option>
                    <option selected value="6:00 PM">6:00 PM</option>
                </select>
                <select required name="schedule-end" id="schedule-end">
                    <option selected value="7:00 AM">7:00 AM</option>
                    <option selected value="7:30 AM">7:30 AM</option>
                    <option selected value="8:00 AM">8:00 AM</option>
                    <option selected value="8:30 AM">8:30 AM</option>
                    <option selected value="9:00 AM">9:00 AM</option>
                    <option selected value="9:30 AM">9:30 AM</option>
                    <option selected value="10:00 AM">10:00 AM</option>
                    <option selected value="10:30 AM">10:30 AM</option>
                    <option selected value="11:00 AM">11:00 AM</option>
                    <option selected value="11:30 AM">11:30 AM</option>
                    <option selected value="12:00 PM">12:00 PM</option>
                    <option selected value="12:30 PM">12:30 PM</option>
                    <option selected value="1:00 PM">1:00 PM</option>
                    <option selected value="1:30 PM">1:30 PM</option>
                    <option selected value="2:00 PM">2:00 PM</option>
                    <option selected value="2:30 PM">2:30 PM</option>
                    <option selected value="3:00 PM">3:00 PM</option>
                    <option selected value="3:30 PM">3:30 PM</option>
                    <option selected value="4:00 PM">4:00 PM</option>
                    <option selected value="4:30 PM">4:30 PM</option>
                    <option selected value="5:00 PM">5:00 PM</option>
                    <option selected value="5:30 PM">5:30 PM</option>
                    <option selected value="6:00 PM">6:00 PM</option>
                    <option selected value="6:30 PM">6:30 PM</option>
                    <option selected value="7:00 PM">7:00 PM</option>
                </select>
            </div>
           <!-- <input type="time" name="schedule-start" step="1800" class="" placeholder="Schedule">
            <input type="time" name="schedule-end" step="1800" class="" placeholder="Schedule">
            <input type="text" name="schedule" class="" placeholder="Schedule">-->
        </div>
        <div class="form-group">
            <label for="room">Room</label>
            <input required type="text" name="room" class="" value="<?php echo $editData['Room']?>" placeholder="Room">
        </div>
        <div class="form-group">
            <label for="subject">Subject</label>
            <input required type="text" name="subject" class="" value="<?php echo $editData['Subject']?>" placeholder="Subject">
        </div>
        <div class="form-group">
            <label for="section">Section</label>
            <input required type="text" name="section" class="" value="<?php echo $editData['Section']?>" placeholder="Section">
        </div>
        <div class="form-group">
            <label for="attendance">Attendance</label>
            <select required name="attendance" id="attendance">
                <option selected value="Present">Present</option>
                <option value="Absent">Absent</option>
                <option value="Late">Late</option>
            </select>
            <!--<input type="text" name="attendance" class="" placeholder="Attendance">-->
        </div>
        <div class="form-group">
            <label for="class-mode">Class Mode</label>
            <select required name="class-mode" id="class-mode">
                <option selected value="F2F">F2F</option>
                <option value="Online">Online</option>
            </select>
            <!--<input type="text" name="class-mode" class="" placeholder="Class Mode">-->
        </div>
        <div class="form-group">
            <input type="submit" name="submit-data" class="" placeholder="">
        </div>
    </form>
</div>
<?php
    
?>
<?php
    $ex='';$ex .= $editData['Schedule'];
    $schedArr = explode(' - ',$ex, 2);
    $schedStart = $schedArr[0];
    $schedEnd = $schedArr[1];
?>
<script>
    const schedStart = document.getElementById('schedule-start');
    const schedEnd = document.getElementById('schedule-end');
    const classModeSel = document.getElementById('class-mode');
    const attendanceSel = document.getElementById('attendance');

    var startTime = '<?php echo $schedStart?>';
    var endTime = '<?php echo $schedEnd ?>';
    var classMode = '<?php echo $editData['ClassMode']?>';
    var attendance = '<?php echo $editData['Attendance'] ?>';
    
    console.log('wew'+classMode);
    console.log('wewddd'+attendance);

    for (let i = 0; i < schedStart.children.length; i++) 
    {
        const optionElement = schedStart.children[i];
        if(optionElement.value === startTime)
            optionElement.selected = true;
    }
    for (let i = 0; i < schedEnd.children.length; i++) 
    {
        const optionElement = schedEnd.children[i];
        if(optionElement.value === endTime)
            optionElement.selected = true;
    }

    for (let i = 0; i < classModeSel.children.length; i++) 
    {
        const optionElement = classModeSel.children[i];
        if(optionElement.value === classMode)
            optionElement.selected = true;
    }
    for (let i = 0; i < attendanceSel.children.length; i++) 
    {
        const optionElement = attendanceSel.children[i];
        if(optionElement.value === attendance)
            optionElement.selected = true;
    }
</script>
<?php
    include('../includes/footer.php');
?>