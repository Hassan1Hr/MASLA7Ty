<?php

class Post extends Database{
    private $imageURL;
    private $postDate;
    private $postTime;
    private $postDescription;
    private $latitude;
    private $longitude;
    private $creatorId;
    
    private $filename = 'include/config.php';
    
    public function __construct($imageURL,$postDate,$posTime,$postDescription,$latitude,$longitude,$creatorId) {
        parent::__construct($this->filename);
    }
    private function setData($imageURL,$postDate,$posTime,$postDescription,$latitude,$longitude,$creatorId){
        $this->imageURL = $imageURL;
        $this->postDate = $postDate;
        $this->postTime = $posTime;
        $this->postDescription = $postDescription;
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->creatorId = $creatorId;
        
    }
    private function connectToDB() {
        $this->connectToDatabase();
    }
    private function post(){
        $stmt = $this->con->prepare("INSERT INTO post(post_photo,post_date,post_time,post_description,latitude,longitude,creator_id) values ( ?,?, ?, ?, ?, ?,?)" );
        $stmt->bind_param("ssssdds",$this->imageURL,  $this->postDate,  $this->postTime,  $this->postDescription,  $this->latitude,  $this->longitude,  $this->creatorId);
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
