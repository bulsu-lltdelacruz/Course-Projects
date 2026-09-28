<?php
    include('../includes/header.php');
    include '../CRUD/read.php';
?>
<style>
    .record-table, .record-table td, .record-table th{
        border: solid black 1px;
        border-collapse: collapse;
        text-align: center;
    }
    .record-table td{
        padding-inline: 5px;
    }
</style>

    <div class="table">
        <h1>Records</h1>
        <table class="record-table">
            <tr>
                <th>Name</th>
                <th>Rank</th>
                <th>Schedule</th>
                <th>Room</th>
                <th>Subject</th>
                <th>Section</th>
                <th>Attendance</th>
                <th>Class Mode</th>
                <th>Action</th>
            </tr>
            <?php
        if(!is_null($records) && count($records) > 1)
        {
            foreach($records as $tag => $dict)
            {
                if($tag == 'index')
                    continue;
                $dict =  json_decode($dict, true);
                ?>
                <tr>
                    <td><?php echo  $dict['Name'];?></td>
                    <td><?php echo  $dict['Rank'];?></td>
                    <td><?php echo  $dict['Schedule'];?></td>
                    <td><?php echo  $dict['Room'];?></td>
                    <td><?php echo  $dict['Subject'];?></td>
                    <td><?php echo  $dict['Section'];?></td>
                    <td><?php echo  $dict['Attendance'];?></td>
                    <td><?php echo  $dict['ClassMode'];?></td>
                    <td>
                        <a href="<?php echo '../Display/editRecord.php?tag='.$tag;?>">Edit</a>
                        <a href="#"  onclick="alert('<?php echo $tag?>')">Delete</a>
                    </td>
                </tr>
            <?php
                //}
            }
        }
        else{
            ?>
            <tr><td colspan="9">No Records To Show</td></tr>
            <?php
            }       
            ?>
        </table>
    </div>
<script>
    function alert(tag)
    {
        let text = 'Are you sure you want to delete the record '+tag+'?';
        let confimed = confirm(text);
        if (confimed)
            window.location.href = `../CRUD/delete.php?tag=${tag}`;
    }
        
</script>
<?php
    include('../includes/footer.php');
?>