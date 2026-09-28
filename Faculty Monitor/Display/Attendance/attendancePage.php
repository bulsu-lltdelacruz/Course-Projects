<?php
include '../../includes/dbConfig.php';
    //echo $tag;
    $ref = 'Schedule';
    $database = new Database();
    $records = $database->getRecords($ref);
    $facultyRecords = $database->getRecords('Faculty');
    $attendanceRecords = $database->getRecords('Attendance');

include '../../includes/header.php';
?>
<script>
    var scheduleRecords = <?php echo json_encode($records);?>;
    var facultyRecords = <?php echo json_encode($facultyRecords);?>;
    var attendanceRecords = <?php echo json_encode($attendanceRecords);?>;
</script>
<style>
    /** style for records */
    .record{
        position: fixed;
        top: 0;
        bottom: 0;
        overflow: auto;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-self: center;
        max-width: 50%;
        min-width: 35%;
        max-height: 50%;
        padding: 20px;
        background-color: white;
        border-radius:16px;box-shadow:0 8px 24px rgba(15,23,42,0.08);
        display: none;
        z-index: 901;
    }
    .count-p{
        color: #6b7280;
        font-size: 12px;
        height: 20px;
    }
    .action-div{
        display: flex;
        flex-direction: row;
        margin-left: auto;
    }
    .realActionDiv{
        display: flex;
        flex-direction: row;
        margin-left: auto;
    }
    .action-update, .action-delete{
        margin-left: auto;
        border: none;
        background-color: transparent;
        padding: 3px;
        margin: 3px;
        font-size: 15px;
        color: #6a5af9;
        text-decoration: underline;
    }
    .record-p{
        align-self: center;
        justify-content: start;
        font-size: 17px;
        cursor: pointer;
    }
    .record-p:hover{
        text-decoration: underline;
    }
    .record > div:nth-child(2) {
        margin-top: 10px;
    }
     .record > div:nth-child(3) {
        margin-top: 10px;
    }
  </style>
<div class="container">
    <?php include 'monthly.php';?>
    <div class="record" id="record-div" name="record-div">
        
        <h3 style="margin-bottom: 15px;"
        >Records</h3>
        <hr>
        <div id="record-content" class="record-names">
            <!--<div class="action-div">
                    <p class="record-p">LEo</p>
                <div class="realActionDiv">
                    <button class="action-update">Update</button>
                    <button class="action-delete">Delete</button>
                </div>
            </div>-->
        </div>
        <div style="
            display: flex;
            flex-direction: row;
            width: 100%;
            height: 40px;
            margin-top: 10px;
        ">
            <button
                style="
                align-self: flex-end;
                margin-left: auto;
                font-size: 15px;
                font-weight: 400;
                height: 40px;"
                    onclick="closeRecords()">Cancel
            </button>
            
        </div>
    </div>
    <?php include 'attendanceAddRecord.php';?>
    <?php // 'attendanceUpdateRecord.php';?>
</div>
  <script>
    document.getElementById('from').value = 'month';
    //document.getElementById('from-edit').value = 'month';

    const MONTH_NAMES = [
      'January','February','March','April','May','June','July','August','September','October','November','December'
    ];
    
    document.title ='Attendance';
    //const dailyView = document.getElementById('prev');

    const calendarEl = document.getElementById('calendar');
    const currentLabel = document.getElementById('currentLabel');
    const prevBtn = document.getElementById('prev');
    const nextBtn = document.getElementById('next');
    const form = document.getElementById('form');
    //const POSTMonth = document.getElementById('month');
    //const POSTDay = document.getElementById('day');
    //const POSTYear = document.getElementById('year');
    const dim = document.getElementById('dim');

    const facultySel = document.getElementById('name');
    const facultySelEdit = document.getElementById('name-edit');
    const recordParent = document.getElementById('record-div');
    const records = document.getElementById('record-content');
    const nnoww = new Date()
    let currentMonth = nnoww.getMonth(); // January
    document.getElementById('title').innerHTML =  MONTH_NAMES[nnoww.getMonth()]+' '+nnoww.getDate()+', '+nnoww.getFullYear();
    const daysOfWeekFull = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];

    const months = [];
    for (let m = 0; m < 12; m++) {
        const section = document.createElement('section');
        section.className = 'month';

        const header = document.createElement('h3');
        header.textContent = `${MONTH_NAMES[m]} 2025`;
        section.appendChild(header);

        const weekdays = document.createElement('div');
        weekdays.className = 'weekdays';
        const daysOfWeek = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
        for (let d of daysOfWeek) 
        {
            const wd = document.createElement('div');
            wd.className = 'weekday';
            wd.textContent = d;
            weekdays.appendChild(wd);
        }
        section.appendChild(weekdays);

        const days = document.createElement('div');
        days.className = 'days';
        section.appendChild(days);

        months.push({section, days});
        calendarEl.appendChild(section);
    }

    function renderMonth(mIndex){
        
        const year = 2025;
        const month = months[mIndex];
        const daysContainer = month.days;
        daysContainer.innerHTML = '';
        
        const first = new Date(year, mIndex, 1);
        const lastDay = new Date(year, mIndex+1, 0).getDate();//last day of previous month, kaya plus 1
        const startWeekday = first.getDay();

        //empty
        for (let i=0; i<startWeekday; i++) {
        const empty = document.createElement('div');
        empty.className = 'day empty';
        empty.innerHTML = '&nbsp;';
        daysContainer.appendChild(empty);
        }
        //days
        
        const today = new Date();
    for (let d=1; d<=lastDay; d++) {
        if(scheduleRecords!=null)
        Object.keys(scheduleRecords).forEach(key => {
            var daysOfWeek = JSON.stringify(scheduleRecords[key]['daysOfWeek']).split(",");
            var monthFrom;
            var monthTo;
            monthFrom =scheduleRecords[key]['monthFrom'];
            monthFrom = monthFrom.substring(monthFrom.length - 2);
            monthFrom = parseInt(monthFrom-1);

            monthTo =scheduleRecords[key]['monthTo'];
            monthTo = monthTo.substring(monthTo.length - 2);
            monthTo = parseInt(monthTo-1);
            
            
          });
        const el = document.createElement('div');
        var p = []; 
        let counter = 0;
        let matched = false;

        
        if(scheduleRecords!=null)
        Object.keys(scheduleRecords).forEach(key => {
            let monthFrom = scheduleRecords[key]['monthFrom'];
            monthFrom = monthFrom.substring(monthFrom.length - 2);
            monthFrom = parseInt(monthFrom-1);

            let monthTo =scheduleRecords[key]['monthTo'];
            monthTo = monthTo.substring(monthTo.length - 2);
            monthTo = parseInt(monthTo-1);

            
            let matched = false;

            let current = new Date(2025, mIndex, d);
            let scheduledaysOfWeek = "";
            let currentDaysOfWeek = "";
            scheduledaysOfWeek = scheduleRecords[key]['daysOfWeek'];
            currentDaysOfWeek = daysOfWeekFull[current.getDay()];
            //const daysOfWeekFull = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
            //HERE

            
            day = current.getDate();
            if(monthFrom <= mIndex && mIndex <= monthTo
                && scheduledaysOfWeek.includes(currentDaysOfWeek)
            )
            {
              
              if(counter >= 4)
              {
                counter++;
                p[p.length-1].innerHTML ='-- '+ (counter-3) + ' more record/s...';
              }else
              {
                p.push(document.createElement('p'));
                p[p.length-1].className = 'count-p';
                counter++;
                var date=(d<=9)?'0'+d:d;
                var month =(mIndex<=9)?'0'+mIndex:mIndex;
                p[p.length-1].innerHTML = '&#x26AA;'+facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                if(attendanceRecords!=null)
                Object.keys(attendanceRecords).forEach(attendanceKey => {
                        
                        var noww = '2025-'+month+'-'+date;
                        var attendanceStatus;
                        var matched = false;
                        if(attendanceRecords[attendanceKey]['scheduleID'] == key
                            && attendanceRecords[attendanceKey]['date'] == noww
                        )
                        {
                            matched = true;
                            attenKey = attendanceKey;
                            attendanceStatus = attendanceRecords[attendanceKey]['attendance'].toUpperCase();
                        }
                        if(matched)
                        {
                            if(attendanceStatus == 'ABSENT')
                                p[p.length-1].innerHTML = '&#128308;'+facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                            else if(attendanceStatus == 'PRESENT' || attendanceStatus == 'EXCUSE' )
                                p[p.length-1].innerHTML = '&#128994;'+facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                            else if(attendanceStatus == 'LATE')
                                p[p.length-1].innerHTML = '&#128992;'+facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                            else
                                p[p.length-1].innerHTML = '&#x26AA;'+facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                        }
                            

                    });
              }
            }
        });
       
      
        
        el.className = 'day';
        el.textContent = d;
        for(let i = 0; i<p.length; i++)
        {   
            el.appendChild(p[i]);
        }
        if(today.getFullYear()===2025 && today.getMonth()===mIndex && today.getDate()===d){
            el.classList.add('today');
        }
        //listener / RECORDS=====================================================================================
        el.addEventListener('click',()=>{
            event.preventDefault();
            event.stopPropagation(); 
            records.innerHTML = '';
            var p;
            var actionDiv;
            var updateBtn;
            var deleteBtn;
            var actionDivReal;
            var token;
            if(scheduleRecords!=null)
    Object.keys(scheduleRecords).forEach(key => {
            let monthFrom =scheduleRecords[key]['monthFrom'];
            monthFrom = monthFrom.substring(monthFrom.length - 2);
            monthFrom = parseInt(monthFrom-1);
            
            let monthTo =scheduleRecords[key]['monthTo'];
            monthTo = monthTo.substring(monthTo.length - 2);
            monthTo = parseInt(monthTo-1);

            let current = new Date(2025, mIndex, d);
            let scheduledaysOfWeek = "";
            let currentDaysOfWeek = "";
            scheduledaysOfWeek = scheduleRecords[key]['daysOfWeek'];
            currentDaysOfWeek = daysOfWeekFull[current.getDay()];
            
            ///////////////IF HAS RECORD AND DATE IS SAME
                if(monthFrom <= mIndex && mIndex <= monthTo
                && scheduledaysOfWeek.includes(currentDaysOfWeek))
                {
                    token = key;
                    p = document.createElement('p');
                    facultyRecords[key]
                    p.textContent = facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                    p.className = 'record-p';

                    actionDiv = document.createElement('div');
                    actionDiv.className = 'action-div';
                    var date=(d<=9)?'0'+d:d;
                    var month =(mIndex<=9)?'0'+mIndex:mIndex;
                    
                    //actionDivReal.appendChild(deleteBtn);
                    var match = false;
                    var attendanceStatus='';
                    var attenKey = '';
                    
                    
                    if(attendanceRecords != null)
                    Object.keys(attendanceRecords).forEach(attendanceKey => {
                        var noww = '2025-'+month+'-'+date;
                        if(attendanceRecords[attendanceKey]['scheduleID'] == key
                            && attendanceRecords[attendanceKey]['date'] == noww
                        )
                        {
                            attenKey = attendanceKey;
                            match = true;
                            attendanceStatus = attendanceRecords[attendanceKey]['attendance'].toUpperCase();
                        }
                    });
                    if(match)
                    {
                        if(attendanceStatus == 'ABSENT')
                            p.innerHTML = '&#128308;'+p.textContent+ ' ('+attendanceStatus+')';
                        else if(attendanceStatus == 'PRESENT' || attendanceStatus == 'EXCUSE' )
                            p.innerHTML = '&#128994;'+p.textContent+ ' ('+attendanceStatus+')';
                        else if(attendanceStatus == 'LATE')
                            p.innerHTML = '&#128992;'+p.textContent+ ' ('+attendanceStatus+')';
                        else
                            p.innerHTML = '&#x26AA;'+p.textContent+ ' ('+attendanceStatus+')';
                    }
                    else
                    {
                        p.innerHTML = '&#x26AA;'+p.textContent;
                    }
                    var stuff = {
                        'date' : '2025-'+month+'-'+date,
                        'scheduleKey' : key,
                        'isUpdate': match,
                        'attendanceKey' : attenKey
                    };
                    
                   // console.log('atten'+attenKey);
                   // console.log('sch'+key);
                    
                    
                    updateBtn = createActionButton('add', stuff);
                    deleteBtn = createActionButton('delete', stuff);

                    actionDivReal = document.createElement('div');
                    actionDivReal.className = 'realActionDiv';
                    
                    actionDivReal.appendChild(updateBtn);
                    actionDivReal.appendChild(deleteBtn);
                    actionDiv.appendChild(p);
                    actionDiv.appendChild(actionDivReal);
                    
                    records.appendChild(actionDiv);
                }
            });
                
            /*POSTMonth.value = MONTH_NAMES[mIndex];
            POSTDay.value = d;
            POSTYear.value = '2025';*/
            recordParent.style.display = 'block';
            dim.style.display = 'block';
            var dih=(d<=9)?'0'+d:d;
            
            
        });
        daysContainer.appendChild(el);
        }

        for(let i=0; i<months.length; i++){
        months[i].section.style.display = (i===mIndex)?'block':'none';
        }

        currentLabel.textContent = `${MONTH_NAMES[mIndex]} 2025`;
        prevBtn.disabled = (mIndex===0);
        nextBtn.disabled = (mIndex===11);
    }

    function createActionButton(type, stuff){
        var button = document.createElement('button');
        button.type = 'button';
        if(type === 'add')
        {
            button.innerHTML = 'Update Attendance'
            button.className = 'action-update';
            button.addEventListener('click', ()=>{
                openAddSchedForm(stuff);
                //console.log(stuff['attendanceKey']+'    dck');
                
                //window.location.href = '../editRecord.php?tag='+token+'&from=calendar';
            });
        }else if(type === 'delete')
        {
            button.innerHTML = 'Delete'
            button.className = 'action-delete';
            button.addEventListener('click', ()=>{
                alert(stuff);
            });
        }
        return button;
    }



    prevBtn.addEventListener('click',(e)=>{e.preventDefault();goTo(-1)});
    nextBtn.addEventListener('click',(e)=>{e.preventDefault();goTo(1)});

    /*window.addEventListener('keydown',(e)=>{
        e.preventDefault();
      if(e.key==='ArrowLeft') goTo(-1);
      if(e.key==='ArrowRight') goTo(1);
    });*/
    function goTo(offset){
    const newIndex = currentMonth + offset;
    currentMonth = newIndex;
      if(newIndex < 0 || newIndex > 11) return;
      
      renderMonth(currentMonth);
    }
    function alert(stuff)
    {
        let text = 'Are you sure you want to delete the record '+stuff['attendanceKey']+'?';
        let confimed = confirm(text);
        if (confimed)
            window.location.href = `../../CRUD/Attendance/attendanceDelete.php?tag=${stuff['attendanceKey']}`+'&from=month';
    }

    renderMonth(currentMonth);

    function openRecords(){
        document.getElementById('record-div').style.display = 'block';
        dim.style.display = 'block';
    }
    function closeRecords(){
        document.getElementById('record-div').style.display = 'none';
        dim.style.display = 'none';
    }
    
    
    function openAddForm(){
      let done =false;
        document.getElementById('form-schedule').style.display = 'block';
        dim.style.display = 'block';
        facultySel.innerHTML = '';
        //if(facultyRecords)
        if(facultyRecords!=null)
        Object.keys(facultyRecords).forEach(key => {
        var middleName = '';
        middleName = facultyRecords[key]['middle-name'];
        var fullName = facultyRecords[key]['first-name'] +' '+  middleName.substring(0,1)+'. '+ facultyRecords[key]['last-name'];
        const option = document.createElement('option');
        option.innerHTML = fullName;
        option.value = key;
        if(!done)
        {
          document.getElementById('faculty-id').value = key;
          done = true;
        }
        
        facultySel.appendChild(option);
      });
    }
    function openAddSchedForm(stuff){
        const edit = new Edit();
        edit.updateAdd(stuff);
    }
    /*******************************/
    function closeAddForm(){
        document.getElementById('form-add').style.display = 'none';
    }
    function closeEditForm(){
        document.getElementById('form-edit').style.display = 'none';
    }
    
  </script>
 <?php
include 'JSedit.php';
?>
<?php
include '../../includes/footer.php';
?>
<script>attendance.className = 'active';</script>
