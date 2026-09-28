<?php
include '../../includes/dbConfig.php';
    //echo $tag;
    $ref = 'Schedule';
    $database = new Database();
    $scheduleRecords = $database->getRecords($ref);
    $facultyRecords = $database->getRecords('Faculty');
    $attendanceRecords = $database->getRecords('Attendance');
    $currentToken = '';
    $token = $_GET['tag'];
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
  .attendance-image{
    max-height: 45px;
  }
  #toggle-show{
    margin-right: auto;
  }
   @media print {
        /* Styles applied only during printing */
        .table-div {
            margin: 0px;
        }
        .arrow{
          display: none;
        }
        .sidebar{
          display: none;
        }
        .side-nav{
          display: none;
        }
        th, td{
          padding: 5px 6px;
          text-align: center;
        }
        #search{
          display: none;
        }
        #toggle-show{
          display: none;
        }
    }
</style>
<div class="container">
  <div class="table-div">
      <h2 style="padding-inline: 5%;"><?php echo $facultyRecords[$token]['first-name']."'s "?>Attendance List</h2>
      <div class="action-filter-search-div">
        <button id="toggle-show" onclick="window.print()">Print</button> 
          <input type="text" name="" id="search" placeholder="Search...">
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
      </tr>
    </thead>
    <tbody>
      <?php
      if(!is_null($attendanceRecords))
        foreach($attendanceRecords as $tag => $value)
        {
          $scheduleID = $value['scheduleID'];
          $facultyID = $scheduleRecords[$scheduleID]['facultyID'];

          if($token == $facultyID)
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
            <img class="attendance-image" src="../../uploads/<?php echo $value['imageName'];?>" alt="Attendance Image">
          </td>
        </tr>
      <?php 
        }
      }
      ?>
    </tbody>
  </table>


  </div>
</div>
<?php include '../../includes/footer.php';?>
<script>
    document.getElementById('reports').className = 'active';

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