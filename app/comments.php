<?php
$postID = 1;//$_POST['postId'];
include '../app/model/Database.php';
include '../app/model/Comments.php';
$comments = new Comments($postID);


