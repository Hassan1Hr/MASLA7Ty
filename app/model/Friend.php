<?php

class Friend extends Database {
    private $userOne;
    private $userTwo;
    private $status;
    private $filename = 'include/config.php';
    
    public function __construct($userOne, $userTwo, $status) {
        parent::__construct($filename);
        $this->setData($userOne, $userTwo, $status);
        $this->connectToDB();
        $this->insertDataIntoDB();
        
    }
    private function setData($userOne, $userTwo, $status) {
        $this->userOne = $userOne; 
        $this->userTwo = $userTwo;
        $this->status = $status;
    }
    private function connectToDB() {
               $this->connectToDatabase();
    }
    private function insertDataIntoDB() {
        $stmt = $this->con->prepare("INSERT INTO masla7tyfinal.friend(user_one, user_two, status) values (?, ?, ?)" );
        $stmt->bind_param("sss",  $this->userOne, $this->userTwo, $this->status);
        $result = $stmt->execute();
        if($result)
        {
           echo '{"success":"1"}';
        }
        else
        {
            echo '{"success":"0"}';
        }
         $stmt->close();
         
    }
    
}
