<?php


class UpdatePassword extends Database{
    private $username;
    private $newPassword;
    private $oldPassword;
    
    private $filename = 'include/config.php';
    
    public function __construct($username,$newPassword,$oldPassword) {
        parent::__construct($this->filename);
        $this->setData($username, $newPassword, $oldPassword);
        $this->connectToDB();
        $this->updatePassword();
        $this->con->close();
    }
    private function setData($username,$newPassword,$oldPassword){
        $this->username = $username;
        $this->newPassword = $newPassword;
        $this->oldPassword = $oldPassword;
    }
    private function connectToDB(){
        $this->connectToDatabase();
    }
    private function updatePassword(){
        
        if($this->confirmPassword()){
        $query = "UPDATE user SET password = ? where username = ?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param('ss', $this->newPassword, $this->username);
        $result = $stmt->execute(); 
        if($result){
            echo '{"success":"1"}';
        }
        else{
            echo '{"success":"0"}';
        }
        }
        else{
            echo '{"success":"0"}';
        }
    }
    private function confirmPassword(){
        $query = "SELECT password FROM user WHERE username = ? AND password = ?";
        $stmt = $this->con->prepare($query);
        $stmt ->bind_param('ss',  $this->username, $this->oldPassword);
        $stmt->execute();
        $stmt->bind_result($password);
        $stmt->store_result();
        
        if($stmt->num_rows == 1){
           
            return true;
        }
        else{
            return false;
        }
        
    }
}
