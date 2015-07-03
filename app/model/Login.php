<?php

class Login extends Database {
    
    private $username;
    private $password;
    private $filename = 'include/config.php';

    public function __construct($username, $password) 
    {
        
        parent::__construct($this->filename);
        $this->setData($username, $password);
        $this->connectToDB();
        $this->getData();
        $this->con->close();
    }
    
    private function setData($username, $password)
    {
        $this->username = $username;
        $this->password = md5($password);
    }
    private function connectToDB()
    {
        $this->connectToDatabase();
    }
    private function getData()
    {
        
        $stmt = $this->con->prepare("select username, password from user where username = ? AND password = ? ");
        $stmt->bind_param('ss',  $this->username, $this->password);
        $stmt->execute();
        $stmt->bind_result($this->username,  $this->password);
        $stmt->store_result();
		if($stmt->num_rows == 1)
		{
                    
                    echo '{"result":"true"}';
		}
		else
		{
                  
                    echo '{"result":"false"}';
                }
    }
}
