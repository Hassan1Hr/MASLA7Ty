<form action="uploadPostImage.php" method="POST" enctype="multipart/form-data">
    Select image to upload:
    <input type="file" name="image">
    <input type="submit" value="Upload" name="submit">
</form>

<?php
if(isset($_POST["submit"])){
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

}
else{
    echo 'll';
}

