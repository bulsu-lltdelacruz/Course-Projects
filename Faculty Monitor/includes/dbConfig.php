<?php
   /* require __DIR__.'/vendor/autoload.php';
    use Kreait\Firebase\Factory;
    use Kreait\Firebase\ServiceAccount;*/
    
    class Database{
        private $firebase;
        
        private $firebaseUrl;
        
        public function __construct(){
            $this->firebaseUrl = "https://attendance-888a8-default-rtdb.firebaseio.com";
            /*$this->firebase = (new Factory)
        ->withServiceAccount(__DIR__.'/attendance-888a8-firebase-adminsdk-fbsvc-fa6e6c5749.json')
        ->withDatabaseUri('https://attendance-888a8-default-rtdb.firebaseio.com')
        ->createDatabase();*/
        }

        public function getRecords($ref)
        {
            $json = file_get_contents("$this->firebaseUrl/$ref.json");
        	$val = json_decode($json, true);
            return $val;
        }

        public function setRecord($ref, $data)
        {
            $options = [
            "http" => [
                "method"  => "PUT",
                "header"  => "Content-Type: application/json",
                "content" => json_encode($data)
            ]
            ];
            $context = stream_context_create($options);
            $result = file_get_contents("$this->firebaseUrl/$ref.json", false, $context);
                return $result;
        }
        public function remove($ref)
        {
            $options = [
                "http" => [
                    "method" => "DELETE"
                ]
            ];
            $context = stream_context_create($options);
            $postdata = file_get_contents("$this->firebaseUrl/$ref.json", false, $context);
            return $postdata;
        }
    
    	 public function updateRecord($ref, $data)
        {
            $options = [
            "http" => [
                "method"  => "PATCH",
                "header"  => "Content-Type: application/json",
                "content" => json_encode($data)
            ]
            ];
            $context = stream_context_create($options);
            $result = file_get_contents("$this->firebaseUrl/$ref.json", false, $context);
                return $result;
        }
        
        /*public function __construct(){
            
            $this->firebase = (new Factory)
        ->withServiceAccount(__DIR__.'/attendance-888a8-firebase-adminsdk-fbsvc-fa6e6c5749.json')
        ->withDatabaseUri('https://attendance-888a8-default-rtdb.firebaseio.com')
        ->createDatabase();
        }

        public function getRecords($ref)
        {
            $reference = $this->firebase->getReference($ref);
            $snapshot = $reference->getSnapshot();
            $records = $snapshot->getValue();

            return $records;
        }

        public function setRecord($ref, $data)
        {
            $postData = $this->firebase->getReference($ref)->set($data);
            return $postData;
        }
        public function remove($ref)
        {
            $postdata =  $this->firebase->getReference($ref)->remove();
            return $postdata;
        }*/
    }
?>