<?php
include '../../includes/dbConfig.php';
    //echo $tag;
    $ref = 'Schedule';
    $database = new Database();
    $records = $database->getRecords($ref);
    $facultyRecords = $database->getRecords('Faculty');

include '../../includes/header.php';
?>
<script>
    var scheduleRecords = <?php echo json_encode($records);?>;
    var facultyRecords = <?php echo json_encode($facultyRecords);?>;
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
        margin-top: 10px;
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
    <?php include 'weekly.php';?>
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
                    onclick="closeEditRecords()">Cancel
            </button>
        </div>
    </div>
    <?php include 'scheduleAddRecord.php';?>
    <?php include 'scheduleUpdateRecord.php';?>
</div>
  <script>
    document.getElementById('from').value = 'week';
    document.getElementById('from-edit').value = 'week';
    /**
     * 
     * var scheduleRecords = 
    var facultyRecords = 
     */
  const calendarEl = document.getElementById('calendar');
  const currentLabel = document.getElementById('currentLabel');
  const prevBtn = document.getElementById('prev');
  const nextBtn = document.getElementById('next');
    const form = document.getElementById('form');
    const POSTMonth = document.getElementById('month');
    const POSTDay = document.getElementById('day');
    const POSTYear = document.getElementById('year');
    const dim = document.getElementById('dim');

    const facultySel = document.getElementById('name');
    const facultySelEdit = document.getElementById('name-edit');
    const recordParent = document.getElementById('record-div');
    const records = document.getElementById('record-content');



  const MONTH_NAMES = [
    'January','February','March','April','May','June','July','August','September','October','November','December'
  ];
  
  let currentDate = new Date(); 

  function getWeekRange(date) {
    const start = new Date(date);
    const day = start.getDay(); // 0=Sun
    start.setDate(start.getDate() - day); // move to Sunday
    const end = new Date(start);
    end.setDate(start.getDate() + 6);
    return [start, end];
  }

  function renderWeek(date) {
    
    calendarEl.innerHTML = '';
    const [start, end] = getWeekRange(date);

    const weekDiv = document.createElement('section');
    weekDiv.className = 'week';

    const labelText = `${MONTH_NAMES[start.getMonth()]} ${start.getDate()} — ${MONTH_NAMES[end.getMonth()]} ${end.getDate()}`;
    const header = document.createElement('h3');
    header.textContent = labelText;
    weekDiv.appendChild(header);

    const weekdays = document.createElement('div');
    weekdays.className = 'weekdays';
    ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'].forEach(d => {
      const wd = document.createElement('div');
      wd.className = 'weekday';
      wd.textContent = d;
      weekdays.appendChild(wd);
    });
    weekDiv.appendChild(weekdays);

    const daysDiv = document.createElement('div');
    daysDiv.className = 'days';
    const daysOfWeekFull = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
    const today = new Date();

    for (let i = 0; i < 7; i++) {
      
      const day = new Date(start);
      day.setDate(start.getDate() + i);
      if(scheduleRecords!=null)
        Object.keys(scheduleRecords).forEach(key => {
      
            var daysOfWeek = JSON.stringify(scheduleRecords[key]['daysOfWeek']).split(",");
            var monthFrom;
            var monthTo;
            monthFrom = scheduleRecords[key]['monthFrom'];
            monthFrom = monthFrom.substring(monthFrom.length - 2);
            monthFrom = parseInt(monthFrom-1);

            monthTo =scheduleRecords[key]['monthTo'];
            monthTo = monthTo.substring(monthTo.length - 2);
            monthTo = parseInt(monthTo-1);
          });
        const el = document.createElement('div');
        var p =[];
        let counter = 0;
        let matched = false;

        
        if(scheduleRecords!=null)
        Object.keys(scheduleRecords).forEach(key => {
            let monthFrom =scheduleRecords[key]['monthFrom'];
            monthFrom = monthFrom.substring(monthFrom.length - 2);
            monthFrom = parseInt(monthFrom-1);

            let monthTo = scheduleRecords[key]['monthTo'];
            monthTo = monthTo.substring(monthTo.length - 2);
            monthTo = parseInt(monthTo-1);

            
            let matched = false;
            let now = new Date();

            let current = new Date(2025, day.getMonth(), day.getDate());
            let scheduledaysOfWeek = "";
            let currentDaysOfWeek = "";
            scheduledaysOfWeek = scheduleRecords[key]['daysOfWeek'];
            currentDaysOfWeek = daysOfWeekFull[current.getDay()];
            //const daysOfWeekFull = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
            //HERE

            if(monthFrom <= day.getMonth() && day.getMonth() <= monthTo
             && scheduledaysOfWeek.includes(currentDaysOfWeek))
            {
              counter++;
              p.push(document.createElement('p'));
              p[p.length-1].className = 'count-p';
              p[p.length-1].innerHTML = facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
              matched =true;
            }
            
        });
      el.className = 'day';
      el.textContent = `${day.getDate()} ${MONTH_NAMES[day.getMonth()]}`;
      for(let i = 0; i<p.length; i++)
      {
        el.appendChild(p[i]);
      }

      if (today.toDateString() === day.toDateString()) {
        el.classList.add('today');
      }

      /************************************************** */
      el.addEventListener('click', () => {
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
            
            let current = new Date(2025, day.getMonth(), day.getDate());
            let scheduledaysOfWeek = "";
            let currentDaysOfWeek = "";
            scheduledaysOfWeek = scheduleRecords[key]['daysOfWeek'];
            currentDaysOfWeek = daysOfWeekFull[current.getDay()];
            //const daysOfWeekFull = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
            //HERE
            
                if(monthFrom <= date.getMonth() && date.getMonth() <= monthTo
                 && scheduledaysOfWeek.includes(currentDaysOfWeek))
                {
                    token = key;
                    p = document.createElement('p');
                    facultyRecords[key]
                    p.textContent = facultyRecords[scheduleRecords[key]['facultyID']]['first-name'];
                    p.className = 'record-p';

                    actionDiv = document.createElement('div');
                    actionDiv.className = 'action-div';

                    updateBtn = createActionButton('update', token);
                    deleteBtn = createActionButton('delete', token);

                    actionDivReal = document.createElement('div');
                    actionDivReal.className = 'realActionDiv';
                    
                    actionDivReal.appendChild(updateBtn);
                    actionDivReal.appendChild(deleteBtn);

                    actionDiv.appendChild(p);
                    actionDiv.appendChild(actionDivReal);
                    
                    records.appendChild(actionDiv);
                }
            });
            openEditRecords();
      });

      daysDiv.appendChild(el);
      
    }

    weekDiv.appendChild(daysDiv);
    calendarEl.appendChild(weekDiv);

    // Week label
    const weekNumber = getWeekNumber(start);
    currentLabel.textContent = `Week ${weekNumber} — ${labelText}`;

    prevBtn.disabled = start.getFullYear() === 2024;
    nextBtn.disabled = end.getFullYear() === 2026;
  }

  function getWeekNumber(date) {
    const firstDay = new Date(date.getFullYear(), 0, 1);
    const days = Math.floor((date - firstDay) / (24 * 60 * 60 * 1000));
    return Math.ceil((days + firstDay.getDay() + 1) / 7);
  }

  function changeWeek(offset) {
    currentDate.setDate(currentDate.getDate() + offset * 7);
    renderWeek(currentDate);
  }

  prevBtn.addEventListener('click', (e) => { e.preventDefault(); changeWeek(-1); });
  nextBtn.addEventListener('click', (e) => { e.preventDefault(); changeWeek(1); });

  renderWeek(currentDate);

  function createActionButton(type, token){
      var button = document.createElement('button');
      button.type = 'button';
      if(type === 'update')
      {
          button.innerHTML = 'Update'
          button.className = 'action-update';
          button.addEventListener('click', ()=>{
              openEditForm(token);
              //window.location.href = '../editRecord.php?tag='+token+'&from=calendar';
          });
      }else if(type === 'delete')
      {
          button.innerHTML = 'Delete'
          button.className = 'action-delete';
          button.addEventListener('click', ()=>{
              alert(token);
          });
      }
      return button;
  }
    function alert(tag)
    {
        let text = 'Are you sure you want to delete the record '+tag+'?';
        let confimed = confirm(text);
        if (confimed)
            window.location.href = `../../CRUD/Schedule/scheduleDelete.php?tag=${tag}`+'&from=week';
    }

  function openEditRecords(){
        document.getElementById('record-div').style.display = 'block';
        dim.style.display = 'block';
    }
    function closeEditRecords(){
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
    function openEditForm(token){
        const edit = new Edit();
        edit.updateEdit(token);
    }
     /*******************************/
    function closeAddForm(){
        document.getElementById('form-schedule').style.display = 'none';
    }
    function closeEditForm(){
        document.getElementById('form-schedule-edit').style.display = 'none';
    }
</script>
  <?php
include 'JSedit.php';
?>
<?php
include '../../includes/footer.php';
?>
<script>schedule.className = 'active';</script>
