<?php
$user_name = $_POST['username'];
include '../app/model/Database.php';
include '../app/model/ProfileInformation.php';
$profile = new ProfileInformation($user_name);
