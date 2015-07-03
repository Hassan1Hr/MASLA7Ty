<?php

class Signup extends Database {
    private $firstName;
    private $lastName;
    private $username;
    private $password;
    private $gender;
    private $age;
    private $country;
    private $city;
    private $user_state;
    private $studiedAt;
    private $mobile;
    private $filename = 'include/config.php';
    public function __construct($first_name,$last_name,$user_name,$pass,$gender,$age,$country,$city,$user_state,$studiedAt,$mobile) {
        
        parent::__construct($this->filename);
        $this->setData($first_name,$last_name,$user_name,$pass,$gender,$age,$country,$city,$user_state,$studiedAt,$mobile);
        $this->connectToDB();
        $this->insertDataInToDB();
        $this->con->close();
        
    }
    private function setData($first_name,$last_name,$user_name,$pass,$gender,$age,$country,$city,$user_state,$studiedAt,$mobile)
    {
        $this->firstName = $first_name;
        $this->lastName = $last_name;
        $this->username = $user_name;
        $this->password = md5($pass);
        $this->gender = $gender;
        $this->age = $age;
        $this->country = $country;
        $this->city = $city;
        $this->user_state = $user_state;
        $this->studiedAt = $studiedAt;
        $this->mobile = $mobile;
        
    }
    private function connectToDB()
    {
        $this->connectToDatabase();
    }
    private function insertDataInToDB()
    {
       // `username`, `password`, `firstName`, `lastName`, `gender`, `age`, `country`, `city`, `user_state`, `studied_at`, `mobile`
        $stmt = $this->con->prepare("INSERT INTO masla7tyfinal.user (username, password, firstName, lastName, gender, age, country, city, user_state, studied_at, mobile)VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param('sssssssssss',  $this->username, $this->password, $this->firstName,  $this->lastName, $this->gender, $this->age, $this->country,  $this->city, $this->user_state,  $this->studiedAt,  $this->mobile);
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
