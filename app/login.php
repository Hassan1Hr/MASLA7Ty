<?php
$user = 'user2';//$_POST["username"];
$pass = 'hoss';//$_POST["password"];
include '../app/model/Database.php';
include '../app/model/Login.php';
$login = new Login($user,$pass);
  
