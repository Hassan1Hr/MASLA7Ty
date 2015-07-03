<?php

class UploadFile {
    private $targetDirection;
    private $targetFile;
    private $file;
    private $fileType;
    private $fileSize;
    private $imageURl;
    
    public function __construct($targetDirection, $targetFile,$file,$fileType,$fileSize) {
        $this->setData($targetDirection, $targetFile,$file,$fileType,$fileSize);
        $this->uploadFile();
        $this->getImageURL();
        
        
    }
    private function setData($targetDirection, $targetFile,$file,$fileType,$fileSize) {
        $this->targetDirection = $targetDirection;
        $this->targetFile = $targetFile;
        $this->file = $file;
        $this->fileType = $fileType;
        $this->fileSize = $fileSize;
        $this->imageURl = ''.$this->targetFile;
       
        
    }
    private function isExist(){
        if(file_exists($this->targetFile)){
            return false;
            
        }
        else{
            return true;
            
        }
    }
    private function checkSize(){
        if($this->fileSize > (500000)){
            
            return false;
            
        }
        else{
            
            return true;
        }
    }
    private function checkExtension(){
        $extensions = array("jpg","png","jpeg","gif");
        if(in_array($this->fileType,$extensions)){
            return true;
            
        }
        else{
            
            return false;
        }
    }

    private function uploadFile() {
        if($this->checkSize()&&$this->checkExtension()&&$this->isExist()){
            if(move_uploaded_file($this->file, $this->targetFile)){
                echo '{"success":"1","result":"file uploaded"}';
            }
            else{
                echo '{"success":"0","result":"error in uploading"}';
            }
            
        }
        else{
            echo '{"success":"0","result":"not suitable for uploading uploading"}';
        }
        
    }
    private function getImageURL(){
        return $this->imageURl;
    }
}
