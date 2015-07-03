<?php
include '../app/model/Database.php';
include '../app/model/Comment.php';
$date = $_POST['postDate'];
$time = $_POST['postTime'];
$description = $_POST['postDescription'];
$creator = $_POST['creatorId'];
$postId = $_POST['postId'];
$comment = new Comment($date, $time, $description, $creator, $postId);

