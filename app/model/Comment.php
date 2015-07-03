<?php

class Comment extends Database {
    
    private $date;
    private $time;
    private $description;
    private $creator;
    private $postID;
    private $filename = 'include/config.php';
    public function __construct($date,$time,$description,$creator,$postID) {
       
        parent::__construct($this->filename);
        $this->setData($date, $time, $description, $creator,$postID);
        $this->connectToDB();
        $this->insertDataIntoDB();
        $this->con->close();
    }
    private function setData($date,$time,$description,$creator,$postID)
    {
        $this->date = $date;
        $this->time = $time;
        $this->description = $description;
        $this->creator = $creator;
        $this->postID =$postID;
    }
    private function connectToDB()
    {
        $this->connectToDatabase();
    }
    private function insertDataIntoDB()
    {
        $stmt = $this->con->prepare("INSERT INTO comment(com_DATE,com_time,com_description,creator_id,post_id)VALUES(?,?,?,?,?)");
        $stmt->bind_param("ssssi",  $this->date,  $this->time,  $this->description,  $this->creator,  $this->postID);
        $result = $stmt->execute();
        if($result)
        {
            echo '{"success":"1"}';
        }
        else
        {
            echo '{"success":"1"}';
        }
        $stmt->close();
    }
    
}
