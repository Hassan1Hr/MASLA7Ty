<?php

class Comments extends Database{
    private $postID;
    
    private $filename = 'include/config.php';
    
    public function __construct($postID) {
        parent::__construct($this->filename);
        $this->setData($postID);
        $this->connectToDB();
        $this->getComments();
        $this->con->close();
        
    }
    private function setData($postID){
        $this->postID = $postID;
    }
    private function connectToDB() {
        $this->connectToDatabase();
    }
    private function getComments() {
        $query = "SELECT comment.com_date,comment.com_time,comment.com_description,user.firstName,user.lastName FROM comment INNER JOIN user ON comment.creator_id = user.username WHERE comment.post_id = ?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("i",  $this->postID);
        $result = $stmt->execute();
        $stmt->bind_result($commentDate,$commentTime,$commentDescription,$firstName,$lastName);
        if($result&&$stmt->fetch()){
            
            
             //echo "$commentDate $commentTime $commentDescription $firstName $lastName";
            $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'commentDescription'=>$commentDescription,'commentDate'=>$commentDate,'commentTime'=>$commentTime);
        while ($stmt->fetch()){
            $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'commentDescription'=>$commentDescription,'commentDate'=>$commentDate,'commentTime'=>$commentTime);
          }
          echo '{"success":"1","posts":'.json_encode($response).'}';
        }
        else{
            '{"success":"0"}';
        }
    }
    
}
