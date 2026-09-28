<?php
include '../../includes/dbConfig.php';
    //echo $tag;
    $ref = 'Schedule';
    $database = new Database();
    $scheduleRecords = $database->getRecords($ref);
    $facultyRecords = $database->getRecords('Faculty');
    $attendanceRecords = $database->getRecords('Attendance');
    $currentToken = '';

include '../../includes/header.php';
?>
<script>
    var scheduleRecords = <?php echo json_encode($scheduleRecords);?>;
    var facultyRecords = <?php echo json_encode(value: $facultyRecords);?>;
    var attendanceRecords = <?php echo json_encode(value: $attendanceRecords);?>;
    var show ='';
</script>
<link rel="stylesheet" href="../table.css">
<style>
  #sortableTable td{
    text-align: center ;
  }
  .table-div{width: 95%;}
   .change-view{
      display: flex;
      flex-direction: row-reverse;
      margin-bottom: 15px;
      margin-inline: 7.5%;
    }
    
    .change-view button{
      background-color: transparent;
      border-radius: 0px;
      font-size: 13px;
      padding: 5px;
      border: 2px solid var(--muted);
      font-weight: 400;
      cursor: pointer;
    }
    .front{
        z-index: 899;
    }
    #toggle-show{
      margin-right: auto;
    }
    .attendance-image{
      max-height: 45px;
    }
</style>
<div class="container">
  <div class="table-div">
      <h2 style="padding-inline: 5%;">Attendance List</h2>
      <div class="action-filter-search-div">
        <button id="toggle-show" onclick="window.location.href = 'attendanceTable.php?show=all'">Show All</button> 
          <input type="text" name="" id="search" placeholder="Search...">
      </div>
      
      <div class="change-view">
            <button class="front" id="weekly">Weekly</button>
            <button class="front" id="monthly">Monthly</button>
            <button class="front" id="table" style="background-color: var(--accent);
      color: var(--card);">Table</button>
        </div>
  <table id="sortableTable">
    <thead>
      <tr>
        <th onclick="sortTable(0, this)">Faculty
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(1, this)">Attendance
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(2, this)">Date
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(3, this)">Room
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(4, this)">Section
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(5, this)">Dress Code
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(6, this)">Meeting Link
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(7, this)">Image
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th>Action
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
      </tr>
    </thead>
    <tbody>
      <?php
      if(!is_null($attendanceRecords))
        foreach($attendanceRecords as $tag => $value)
        {
          $date = new DateTime();
          $date->modify('-1 month');
          $currentDate = $date->format('Y-m-d');  
          $recordDate = $value['date'];

          if(isset($_GET['show']))
          {
            $show=$_GET['show'];
            echo "<script> var show = '$show'</script>";
            if($show=='all')
            {
              $currentDate = 'same';
              $recordDate = 'same';
            }else{

            }
          }
          
          
          if($recordDate == $currentDate)
          {
        ?>
        <tr>
          <td>
            <?php echo $facultyRecords[$scheduleRecords[$value['scheduleID']]['facultyID']]['first-name'] ;?>
          </td>
          <td>
            <?php echo $value['attendance'];?>
          </td>
          <td>
            <?php echo $value['date'];?>
          </td>
          <td>
            <?php echo $scheduleRecords[$value['scheduleID']]['room'];?>
          </td>
          <td>
            <?php echo $scheduleRecords[$value['scheduleID']]['section'] ;?>
          </td>
          <td>
            <?php echo $value['dressCode'];?>
          </td>
          <td>
            <?php echo $value['link'];?>
          </td>
          <td>
            <img class="attendance-image" src="<?php echo $value['imageName'];?>" alt="Attendance Image">
          </td>
          <td>
            <a href="#" onclick="openEditForm(
            '<?php echo $tag?>', 
            '<?php echo $value['scheduleID']?>',
            true,
            '<?php echo $value['date']?>'
            )">Update</a>
            <a href="#" onclick="alert('<?php echo $tag?>')">Delete</a>
          </td>
        </tr>
      <?php 
        }
      }
      ?>
    </tbody>
  </table>


  </div>
    <?php include 'attendanceAddRecord.php';?>
</div>
<?php include '../../includes/footer.php';?>
<script>

    document.getElementById('from').value = 'table';
    
    if(show == 'all')
    {
      document.getElementById('toggle-show').addEventListener('click', ()=>window.location.href = 'attendanceTable.php?show=');
      document.getElementById('toggle-show').innerHTML = 'Show Today';
    }
    
 const monthly = document.getElementById('monthly');
  const weekly = document.getElementById('weekly');
  table.addEventListener('click', ()=>{
    //window.location.replace('attendanceTable.php');
  });
  monthly.addEventListener('click', ()=>{
    window.location.replace('attendancePage.php');
  });
  weekly.addEventListener('click', ()=>{
   window.location.replace('weeklyAttendancePage.php');
  });
  function closeDim(){
    document.getElementById('dim').style.display = 'none';
  }

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


  function showAddForm(){
    document.getElementById('form-faculty').style.display = 'flex';
    const dim = document.getElementsByClassName('dim');
    for(i=0;i<dim.length;i++){
      dim[i].style.display = 'block';
    }
    
  }
  function closeAddForm(){
    document.getElementById('form-faculty').style.display = 'none';
    const dim = document.getElementsByClassName('dim');
    for(i=0;i<dim.length;i++){
      dim[i].style.display = 'none';
    }
  }
  function showEditForm(token){
    document.getElementById('form-faculty-edit').style.display = 'flex';
    document.getElementById('token').value = token;
    var passedRec = records[token];

    document.getElementById('id-num-edit').value = passedRec['id-num'];
    document.getElementById('last-name-edit').value =  passedRec['last-name'];
    document.getElementById('first-name-edit').value =  passedRec['first-name'];
    document.getElementById('middle-name-edit').value =  passedRec['middle-name'];
    document.getElementById('department-edit').value =  passedRec['department'];
    updateSelectEditForm(passedRec);
    
    document.getElementById('form-faculty-edit').style.display = 'flex';
    const dim = document.getElementsByClassName('dim');
    for(i=0;i<dim.length;i++){
      dim[i].style.display = 'block';
    }
  }
  function updateSelectEditForm(passedRec){
    
    const genderSel = document.getElementById('gender-edit');
    var gender;
    gender = passedRec['gender'];
    const rankSel = document.getElementById('rank-edit');
    var rank;
    rank = passedRec['rank'];
    const statusSel = document.getElementById('faculty-status-edit');
    var status;
    status = passedRec['faculty-status'];
    
    for (let i = 0; i < genderSel.children.length; i++) 
    {
        const optionElement = genderSel.children[i];
        
        if(optionElement.value == gender)
            optionElement.selected = true;
    }

    for (let i = 0; i < rankSel.children.length; i++) 
    {
        const optionElement = rankSel.children[i];
        
        if(optionElement.value == rank)
            optionElement.selected = true;
    }
    for (let i = 0; i < statusSel.children.length; i++) 
    {
        const optionElement = statusSel.children[i];
        
        if(optionElement.value == status)
            optionElement.selected = true;
    }
    
  }
  
  function closeEditForm(){
    document.getElementById('form-faculty-edit').style.display = 'none';
    const dim = document.getElementsByClassName('dim');
    for(i=0;i<dim.length;i++){
      dim[i].style.display = 'none';
    }
  }
  function deleteAllCookies() {
    document.cookie.split(';').forEach(cookie => {
        const eqPos = cookie.indexOf('=');
        const name = eqPos > -1 ? cookie.substring(0, eqPos) : cookie;
        document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT';
    });
}
  
  
</script>
<script>
  document.getElementById('close-form').addEventListener('click', ()=>{
    closeAddForm();
    closeDim();
  });
let currentSortColumn = null;
let ascending = true;

function sortTable(columnIndex, headerElement) {
  const table = document.getElementById("sortableTable");
  const tbody = table.tBodies[0];
  const rows = Array.from(tbody.rows);

  // Toggle sort direction if same column clicked
  if (currentSortColumn === columnIndex) {
    ascending = !ascending;
  } else {
    ascending = true;
    currentSortColumn = columnIndex;
  }

  // Reset all arrow indicators
  document.querySelectorAll(".arrow").forEach(arrow => arrow.classList.remove("active"));

  // Highlight active arrow
  const arrows = headerElement.querySelector(".sort-arrows");
  if (ascending) {
    arrows.querySelector(".up").classList.add("active");
  } else {
    arrows.querySelector(".down").classList.add("active");
  }

  // Sort rows
  rows.sort((a, b) => {
    const cellA = a.cells[columnIndex].innerText.toLowerCase();
    const cellB = b.cells[columnIndex].innerText.toLowerCase();

    if (!isNaN(cellA) && !isNaN(cellB)) {
      return ascending ? cellA - cellB : cellB - cellA;
    } else {
      return ascending
        ? cellA.localeCompare(cellB)
        : cellB.localeCompare(cellA);
    }
  });

  // Append sorted rows
  rows.forEach(row => tbody.appendChild(row));
}
/* SEARCHH*/
document.getElementById("search").addEventListener("input", function () {
  const input = this.value.toLowerCase().trim();
  const table = document.getElementById("sortableTable");
  const rows = table.tBodies[0].rows;

  for (let i = 0; i < rows.length; i++) {
    const cells = rows[i].getElementsByTagName("td");
    let match = false;

    for (let j = 0; j < cells.length; j++) {
      if (cells[j].innerText.toLowerCase().includes(input)) {
        match = true;
        break;
      }
    }

    rows[i].style.display = match ? "" : "none";
  }
});
/**********************8888==================================================================== */
    
    function alert(token)
    {
        let text = 'Are you sure you want to delete the record '+token+'?';
        let confimed = confirm(text);
        if (confimed)
            window.location.href = `../../CRUD/Attendance/attendanceDelete.php?tag=${token}&from=table`;
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
    function openEditForm(attenKey, schedkey, isUpdate, date){
      stuff = {
          'date' : date,
          'scheduleKey' : schedkey,
          'isUpdate': isUpdate,
          'attendanceKey' : attenKey
      };
      const edit = new Edit();
        edit.updateAdd(stuff);
        /*const edit = new Edit();
        edit.updateEdit(token);*/
    }
     /*******************************/
    function closeAddForm(){
        document.getElementById('form-add').style.display = 'none';
    }
    function closeEditForm(){
        document.getElementById('form-schedule-edit').style.display = 'none';
    }
    function closeDim(){
    document.getElementById('dim').style.display = 'none';
  }
</script>
  <?php
include 'JSedit.php';
?>
<script>
    attendance.className = 'active';
</script>