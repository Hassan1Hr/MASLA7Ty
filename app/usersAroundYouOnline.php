<?php

include '../app/model/Database.php';
include '../app/model/UsersAroundYouOnline.php';
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$users = new UsersAroundYouOnline($latitude, $longitude);

