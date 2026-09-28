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
            <a href="facultyReport.php?tag=<?php echo $tag;?>">Show Report</a>
          </td>
        </tr>
      <?php 
        }
      ?>
    </tbody>
  </table>


  </div>
</div>
<?php include '../../includes/footer.php';?>
<script>
    document.getElementById('reports').className = 'active';

     // Table search functionality
  const searchInput = document.getElementById('search');
  const table = document.getElementById('sortableTable');
  const tbody = table.getElementsByTagName('tbody')[0];

  searchInput.addEventListener('keyup', function() {
      const filter = this.value.toLowerCase();
      const rows = tbody.getElementsByTagName('tr');

      for (let i = 0; i < rows.length; i++) {
          const cells = rows[i].getElementsByTagName('td');
          let match = false;

          for (let j = 0; j < cells.length - 1; j++) { // ignore last column (action)
              if (cells[j].textContent.toLowerCase().indexOf(filter) > -1) {
                  match = true;
                  break;
              }
          }

          rows[i].style.display = match ? '' : 'none';
      }
  });
</script>