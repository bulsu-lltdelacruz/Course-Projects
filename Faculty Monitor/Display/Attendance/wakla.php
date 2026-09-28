<link rel="stylesheet" href="../form.css">
<div id="form-schedule" class="form-div">
    <div class="top">
        <h2>Faculty Schedule</h2>
    </div>
    <form id="form" action="../../CRUD/Schedule/scheduleInsert.php" method="post">
        <!---->
    <input style="display: none;" type="text" name="faculty-id" id="faculty-id">
        <input style="display: none;" type="text" name="month" id="month">
        <input style="display: none;" type="text" name="day" id="day">
        <input style="display: none;" type="text" name="year" id="year">
        <input style="display: none;" type="text" name="from" id="from">
        <!---->
        <div class="left-side">
            <div class="form-group">
                <label for="name">Faculty</label>
                <select name="name" id="name">

                </select>
                <!--<input required type="text" name="name" id="name" class="" placeholder="Name">-->
            </div>
            <div class="form-group">
                <label for="section">Section</label>
                <input required type="text" name="section" id="section" placeholder="Section">
            </div>
            <div class="form-group">
                <label for="subject">Subject</label>
                <input required type="text" name="subject" id="subject" class="" placeholder="Subject">
            </div>
            <div class="form-group">
                <label for="room">Room</label>
                <input required type="text" name="room" id="room" class="" placeholder="Room">
            </div>
            <div class="form-group">
                <label for="class-mode">Class Mode</label>
                <select required name="class-mode" id="class-mode">
                    <option selected value="F2F">F2F</option>
                    <option value="Online">Online</option>
                </select>
            </div>
            <div class="form-group">
                <label for="section">Online Meeting Link</label>
                <input required type="text" name="link" placeholder="Link">
            </div>


            <div class="form-group" id="submission-group">
                <input id="submit-form" type="submit" name="submit-data" class="submit-btn" placeholder="">
                <button type="button" id="close-form" class="submit-btn" onclick="closeAddForm(); closeDim();">Cancel</button>
            </div>
        </div><!--==================================================END OF LEFT SIDE=================================-->
        <div class="right-side">
            
            <div class="form-group" id="day-select-group">
                <label for="days-of-week">Days of The Week</label>
                <input type="text" required name="days-of-week" id="days-of-week" placeholder="Days of the Week">
            </div>
            <div id="day-select" class="form-group" style="display: none;">
                <div id="day-select-dropdown" class="day-selection">
                    <div>
                        <input id="monday" value="Monday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="monday">Monday</label>
                    </div>
                    <div>
                        <input id="tuesday" value="Tuesday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="tuesday">Tuesday</label>
                    </div>
                    <div>
                        <input id="wednesday" value="Wednesday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="wednesday">Wednesday</label>
                    </div>
                    <div>
                        <input id="thursday" value="Thursday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="thursday">Thursday</label>
                    </div>
                    <div>
                        <input id="friday" value="Friday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="friday">Friday</label>
                    </div>
                    <div>
                        <input id="saturday" value="Saturday" name="day-select" type="checkbox">
                        <label class="check-box-label" for="saturday">Saturday</label>
                    </div>
                    <button id="confirm-day-select" type="button" style="font-size: 12px; margin-right: auto; border: solid black 0.5px; padding: 5px; border-radius:0px;">Ok</button>
                </div>
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
        </div>
        
    </form>
    
</div>
<script>
    const daySelect = document.getElementById('day-select');
    const dayInput = document.getElementById('days-of-week');
    const daySelDropDown = document.getElementById('day-select-dropdown');
    const daySelGroup = document.getElementById('day-select-group');
    const confirmDaySel = document.getElementById('confirm-day-select');
    const submit = document.getElementById('submit-formt');

    const daySelectionArray = [];
    changedInput = false;
    dayInput.readOnly = true;
    dayInput.style.display =false;
    daySelGroup.addEventListener('focusin',()=>
    {
        daySelect.style.display = 'flex';
    });
    confirmDaySel.addEventListener('click',()=>
    {
        dayInput.value = daySelectionArray;
        daySelect.style.display = 'none';
    });
    
    daySelDropDown.childNodes.forEach(elem => 
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
        
    });
    /*submit.addEventListener('click',(e)=>{
        const readonly = document.getElementsByClassName('readOnly');
        e.preventDefault();

    });*/
    
</script>
