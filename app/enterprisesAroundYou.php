<?php
$rad = 10;//$_POST['radius'];
$lat =27.186949;// $_POST['latitude'];
$long = 31.175232;//$_POST['longitude'];
include '../app/model/Database.php';
include '../app/model/EnterprisesAroundYou.php';
$enterprises = new EnterprisesAroundYou($lat, $long, $rad);

