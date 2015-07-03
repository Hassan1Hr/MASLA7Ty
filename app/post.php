<?php

include '../app/model/Database.php';
include '../app/model/Post.php';
include '../app/model/UploadFile.php';

if ($_FILES){
    $targetDirection = "uploads/";
    $targetFile = $targetDirection . basename($_FILES["image"]["name"]);
    $file =$_FILES['image']['tmp_name'];
    $targetPath = $targetPath . basename($_FILES['image']['name']);
    $fileType = pathinfo($targetFile,PATHINFO_EXTENSION);
    $fileSize = $_FILES['image']['size'];
    $uploadImage = new UploadFile($targetDirection, $targetFile,$file,$fileType,$fileSize);
    
}

$imageURL =$uploadImage ;
$postDate = $_POST['postDate'];
$postTime = $_POST['postTime'];
$postDescription = $_POST['postDescription'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$creatorId = $_POST['creatorId'];

$post = new Post($imageURL, $postDate, $posTime, $postDescription, $latitude, $longitude, $creatorId);