<?php
include ('includes/dpConfig.php');
if(isset($_POST['submit']))
{
    $name = $_POST['username'];
    $room = $_POST['room'];

    $data = [
        'name' => $name,
        'room' => $room
    ];
   

}
    $ref = 'Real/';
    $reference = $firebase->getReference($ref);
    $snapshot = $reference->getSnapshot();
    $value = $snapshot->getValue();
    $gago = 'potek';
?>
<?php
    $gago = 'potek';
?>
<html>
    <div>
        <h1>
            <?php 
			foreach($value as $key => $row){
                echo     $key.'gago';
            }
			?>
        </h1>
    </div>
</html>