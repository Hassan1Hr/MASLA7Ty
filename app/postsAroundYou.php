<?php
include '../app/model/Database.php';
include '../app/model/PostsAroundYou.php';
$lat =27.186949;// $_POST['latitude'];
$long = 31.175232;//$_POST['longitude'];
$posts = new PostsAroundYou($lat, $long);

