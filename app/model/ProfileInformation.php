<?php

class ProfileInformation extends Database {
    private $username;
    private $filename = 'include/config.php';
    
    public function __construct($username) {
        parent::__construct($this->filename);
        $this->setdata($username);
        $this->connectToDB();
        $this->displayData();
        
        
   }
   
   private function setdata($username){
       $this->username = $username;
   }
   private function connectToDB(){
       $this->connectToDatabase();
   }
   private function displayData() {
       $stmt = $this->con->prepare("SELECT firstName, lastName, username from user WHERE username = ?");
       $stmt->bind_param("s", $this->username);
       $result = $stmt->execute();
       $stmt->bind_result($firstName, $lastName, $username);
       //$stmt->fetch();
       if($result&&$stmt->fetch()){
           
           echo '{"success":"1","firstName":"'.$firstName.'","lastName":"'.$lastName.'","username":"'.$username.'"}';
       }
       else {
           echo '{"success":"0"}';
       }
   }
}
