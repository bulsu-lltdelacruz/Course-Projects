<?php
include '../../includes/dbConfig.php';
    //echo $tag;
    $ref = 'Faculty';
    $database = new Database();
    $records = $database->getRecords($ref);
    $currentToken = '';

include '../../includes/header.php';
?>
<script>
    var records = <?php echo json_encode($records);?>;
</script>
<link rel="stylesheet" href="../table.css">
<style>
  .table-div{width: 95%;}
</style>
<div class="container">
  <div class="table-div">
      <h2>Faculty List</h2>
      <div class="action-filter-search-div">
          <input type="text" name="" id="search" placeholder="Search...">
          <button class="primary-btn"onclick="showAddForm()" id="add-faculty">Add &plus;</button>
      </div>
  <table id="sortableTable">
    <thead>
      <tr>
        <th onclick="sortTable(0, this)">#
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(1, this)">ID No
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(2, this)">Name
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(3, this)">Department
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(4, this)">Faculty Rank
          <span class="sort-arrows">
            <span class="arrow up">▲</span>
            <span class="arrow down">▼</span>
          </span>
        </th>
        <th onclick="sortTable(5, this)">Faculty Status
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
      $count = 1;
      if(!is_null($records))
        foreach($records as $tag => $value)
        {
        ?>
        <tr>
          <td>
            <?php echo $count; $count++;?>
          </td>
          <td>
            <?php echo $value['id-num'];?>
          </td>
          <td>
            <?php echo $value['first-name'].' '.substr($value['middle-name'],0,1).'. '.$value['last-name'];?>
          </td>
          <td>
            <?php echo $value['department'];?>
          </td>
          <td>
            <?php echo $value['rank'];?>
          </td>
          <td>
            <?php echo $value['faculty-status'];?>
          </td>
          <td>
            <a href="#" onclick="<?php $currentToken = $tag?>showEditForm('<?php echo $tag?>')">Update</a>
            <a href="#" onclick="alert('<?php echo $tag?>')">Delete</a>
          </td>
        </tr>
      <?php 
        }
      ?>
    </tbody>
  </table>


  </div>
  <?php include 'facultyAddRecord.php';?>
  <?php include 'facultyEditRecord.php';?>
</div>
<?php include '../../includes/footer.php';?>
<script>
  function alert(tag)
    {
        let text = 'Are you sure you want to delete the record '+tag+'?';
        let confimed = confirm(text);
        if (confimed)
            window.location.href = `../../CRUD/Faculty/deleteFaculty.php?tag=${tag}`+'&from=calendar';
    }
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
</script>

<script>
    facultyList.className = 'active';
</script>