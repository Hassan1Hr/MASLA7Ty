<?php

class PostsAroundYou extends Database {
    private $latitude;
    private $longitude;
    private $radius = 1;
    private $filename = 'include/config.php';
    
    public function __construct($latitude,$longitude) {
        parent::__construct($this->filename);
        $this->setData($latitude, $longitude);
        $this->connectToDB();
        $this->getPosts();
        $this->con->close();
    }
    private function setData($latitude,$longitude) {
        $this->latitude = $latitude;
        $this->longitude = $longitude;
    }
    private function connectToDB(){
        $this->connectToDatabase();
    }
    private function getPosts(){
        $query=(" SELECT post.post_date, post.post_time, post.post_description, user.firstName, user.lastName, ( 6371 * acos( cos( radians( ? ) ) * cos( radians( post.latitude ) ) * cos( radians( post.longitude ) - radians( ? ) ) + sin( radians( ? ) ) * sin( radians( post.latitude ) ) ) ) AS distance FROM post  INNER JOIN user ON post.creator_id = user.username HAVING distance <? ORDER BY distance LIMIT 0 , 20 ");
                
               
                
                
                
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("dddd",  $this->latitude,  $this->longitude,  $this->latitude,  $this->radius);
        $result = $stmt->execute();
        $stmt->bind_result($postDate,$postTime,$postDescription,$firstName,$lastName,$distance);
        if($result&&$stmt->fetch()){
            $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'postDescription'=>$postDescription,'postDate'=>$postDate,'postTime'=>$postTime);
            while ($stmt->fetch()){
                $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'postDescription'=>$postDescription,'postDate'=>$postDate,'postTime'=>$postTime);
         
            }
            echo '{"success":"1","posts":'.json_encode($response).'}';
        }
        else{
             echo '{"success":"0"}';
        }
        
    }
}
