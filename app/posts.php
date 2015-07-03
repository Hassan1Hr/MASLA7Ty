<?php
$user_name = 'user1';//$_POST['username'];
include '../app/model/Database.php';
include '../app/model/Posts.php';
$posts = new Posts($user_name);
