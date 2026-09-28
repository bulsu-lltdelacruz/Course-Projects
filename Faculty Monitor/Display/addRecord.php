<?php
    include '../includes/header.php';
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
<div class="form-div">
    <form  action="../CRUD/insert.php" method="post">
        <h1>Add Record</h1>
        <div class="form-group">
            <label for="name">Name</label>
            <input required type="text" name="name" class="" placeholder="Name">
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
            <input required type="text" name="room" class="" placeholder="Room">
        </div>
        <div class="form-group">
            <label for="subject">Subject</label>
            <input required type="text" name="subject" class="" placeholder="Subject">
        </div>
        <div class="form-group">
            <label for="section">Section</label>
            <input required type="text" name="section" class="" placeholder="Section">
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
    include '../includes/footer.php';
?>