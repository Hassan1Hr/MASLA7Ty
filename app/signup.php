<?php
include '../app/model/Database.php';
include '../app/model/Signup.php';
$first_name = $_POST['firstName'];
$last_name = $_POST['lastName'];
$user_name = $_POST['username'];
$pass = $_POST['password'];
$gender = $_POST['gender'];
$age = $_POST['age'];
$country= $_POST['country'];
$city = $_POST['city'];
$user_state = $_POST['user_state'];
$studiedAt = $_POST['studied_at'];
$mobile = $_POST['mobile'];
$signup = new Signup($first_name,$last_name,$user_name,$pass,$gender,$age,$country,$city,$user_state,$studiedAt,$mobile);


