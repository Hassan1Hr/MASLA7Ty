<?php

include '../app/model/Database.php';
include '../app/model/UpdatePassword.php';
$user_name =$_POST['username'];// 'user2';
$npass = $_POST['newPassword'];//'123asd';
$opass = $_POST['oldPassword'];//'123asden';
$updater = new UpdatePassword($user_name,$npass,$opass);

