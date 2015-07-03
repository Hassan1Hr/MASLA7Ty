<?php
class Database {
   
    private $host;
    private $username;
    private $password;
    private $database;
    protected $con;


    protected function __construct($filename) {
        if(is_file($filename))
        {
            
            include 'include/config.php';
            $this->host = $host;
            $this->username = $username;
            $this->password = $password;
            $this->database = $database;
            //$this->connectToDatabase();

        }
        else
        {
            echo 'This not a file';
        }
    }
    
    protected function connectToDatabase(){
        $this->con = new mysqli($this->host,  $this->username, $this->password, $this->database);
        
 
    }
    
}
