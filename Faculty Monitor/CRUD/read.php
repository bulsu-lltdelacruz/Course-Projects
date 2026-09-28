<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
?>

<?php

include ('../includes/dbConfig.php');
    $ref = 'Real/';
    $database = new Database();
    /*$reference = $database->firebase->getReference($ref);
    $snapshot = $reference->getSnapshot();*/
    $records =$database->getRecords($ref);
?>

<?php
//////// DO NOT DELETE
   /* require __DIR__.'/vendor/autoload.php';
    use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;

    //$serviceAccount = ServiceAccount::
    $firebase = (new Factory)
    ->withServiceAccount(__DIR__.'/attendance-888a8-firebase-adminsdk-fbsvc-fa6e6c5749.json')
    ->withDatabaseUri('https://attendance-888a8-default-rtdb.firebaseio.com')
    ->createDatabase();
    
    $database = $firebase->getReference();
*/
    
?>