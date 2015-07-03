<?php

class Posts extends Database {
    private $username;
    
    private $filename = 'include/config.php';
    
    public function __construct($username) {
        parent::__construct($this->filename);
        $this->setData($username);
        $this->connectToDB();
        $this->getPosts();
        $this->con->close();
        
    }
    private function setData($username){
        $this->username = $username;
    }
    private function connectToDB(){
        $this->connectToDatabase();
    }
    private function getPosts(){
        $query = "SELECT post_date, post_time, post_description  FROM post WHERE creator_id = ?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("s",  $this->username);
        $result = $stmt->execute();
        $stmt->bind_result($postDate, $postTime,$postDescription);
        if($result&&$stmt->fetch()){
            $response[] = array('postDescription'=>$postDescription,'postDate'=>$postDate,'postTime'=>$postTime);
            while ($stmt->fetch()){
             $response[] = array('postDescription'=>$postDescription,'postDate'=>$postDate,'postTime'=>$postTime);
            }
            echo '{"success":"1","posts":'.json_encode($response).'}';
        }
        else{
            echo '{"success":"0"}';
        }
        
    }
    
}
