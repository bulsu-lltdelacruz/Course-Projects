<link rel="stylesheet" href="../form.css">
<div id="form-add" class="form-div">
    <div class="top">
        <h2>Add Faculty Schedule</h2>
    </div>
    <form id="form" action="../../CRUD/Attendance/attendanceInsert.php" method="post" enctype="multipart/form-data">
        <!---->
        <input style="display: none;" type="text" name="token" id="token">
        <input style="display: none;" type="text" name="token-attendance" id="token-attendance">
        <input style="display: none;" type="text" name="from" id="from">
        <!---->
        <div class="left-side">
            <div class="form-group">
                <label for="name">Faculty</label>
                <input readonly name="name" id="name">
                <!--<input required type="text" name="name" id="name" class="" placeholder="Name">-->
            </div>
            <div class="form-group">
                <label for="section">Section</label>
                <input readonly required type="text" name="section" id="section" placeholder="Section">
            </div>
            <div class="form-group">
                <label for="subject">Subject</label>
                <input readonly required type="text" name="subject" id="subject" class="" placeholder="Subject">
            </div>
            <div class="form-group">
                <label for="room">Room</label>
                <input readonly required type="text" name="room" id="room" class="" placeholder="Room">
            </div>
            <div class="form-group">
                <label for="class-mode">Class Mode</label>
                <input readonly required name="class-mode" id="class-mode">
            </div>
            <div class="form-group" id="day-select-group">
                <label for="days-of-week">Days of The Week</label>
                <input type="text" readonly required name="days-of-week" id="days-of-week" placeholder="Days of the Week">
            </div>
            <div class="form-group">
                <label for="schedule">Schedule</label>
                <div class="selection-group">
                    <input readonly required name="schedule-start" id="schedule-start">
                    <input readonly required name="schedule-end" id="schedule-end">
                </div>
            </div>


            <div class="form-group" id="submission-group">
                <input id="submit-form" type="submit" name="submit-data" class="submit-btn" placeholder="">
                <button type="button" id="close-form" class="submit-btn" onclick="closeAddForm();">Cancel</button>
            </div>
        </div><!--==================================================END OF LEFT SIDE=================================-->
        <div class="right-side">
            <div class="form-group">
                <label for="date">Attendance Date</label>
                <input required readonly readonly type="date" id="date" name="date" placeholder="Date">
            </div>
            <div class="form-group">
                <label for="link">Online Meeting Link</label>
                <input required type="text" id="link" name="link" placeholder="Link">
            </div>
            <div class="form-group">
                <label for="attendance-status">Attendance</label>
                <select name="attendance-status" id="attendance-status">
                    <option value="Present">Present</option>
                    <option value="Absent">Absent</option>
                    <option value="Late">Late</option>
                    <option value="Excuse">Excuse</option>
                </select>
            </div>
             <div class="form-group">
                <label for="dress-code">Dress Code</label>
                <select name="dress-code" id="dress-code">
                    <option value="Filipiniana">Filipiniana</option>
                    <option value="Asean">Asean</option>
                    <option value="Uniform">Uniform</option>
                    <option value="Casual">Casual</option>
                    <option value="N/A">N/A</option>
                </select>
            </div>
            <div class="form-group">
                <label for="remarks">Remarks</label>
                <input type="text" id="remarks" name="remarks" placeholder="Remarks">
            </div>
            <div class="form-group">
                <label for="photo">Image</label>
                <input required type="file" id="photo" name="photo" accept="image/*">
            </div>
        </div>
        
    </form>
    
</div>
<script>
    const daySelectEdit = document.getElementById('day-select');
    const dayInputEdit = document.getElementById('days-of-week');
    const daySelDropDownEdit = document.getElementById('day-select-dropdown');
    const daySelGroupEdit = document.getElementById('day-select-group');
    const confirmDaySelEdit = document.getElementById('confirm-day-select');
    const submitEdit = document.getElementById('submit-form');

    const daySelectionArrayEdit = [];
    changedInputEdit = false;
    dayInputEdit.readOnly = true;
    dayInputEdit.style.display =false;
   /* daySelGroupEdit.addEventListener('focusin',()=>
    {
        daySelectEdit.style.display = 'flex';
    });*/
    /*confirmDaySelEdit.addEventListener('click',()=>
    {
        dayInputEdit.value = daySelectionArray;
        daySelectEdit.style.display = 'none';
    });*/
    
    /*daySelDropDownEdit.childNodes.forEach(elem => 
    {
        elem.childNodes.forEach(el => 
        {
            el.addEventListener('change', (e)=>{
                console.log(e.target.value);
                
                if(e.target.checked)
                {
                    daySelectionArray.push(e.target.value);
                    
                }else{
                    daySelectionArray.splice(daySelectionArray.indexOf(e.target.value),1);
                }console.log(daySelectionArray);
                
            });
                
        });
        
    });*/
    /*submit.addEventListener('click',(e)=>{
        const readonly = document.getElementsByClassName('readOnly');
        e.preventDefault();

    });*/
    
</script>
