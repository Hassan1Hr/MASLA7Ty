<?php

$userOne = $_POST['username1'];
$userTwo = $_POST['username2'];
$status = $_POST['status'];
include '../app/model/Database.php';
include '../app/model/Friend.php';
$friend = new Friend($userOne, $userTwo, $status);

