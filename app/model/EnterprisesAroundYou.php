<?php

class EnterprisesAroundYou extends Database{
   
    private $latitude;
    private $longitude;
    private $radius;
    
    private $filename = 'include/config.php';
    
    public function __construct($latitude, $longitude, $radius) {
        parent::__construct($this->filename);
        $this->setData($latitude, $longitude, $radius);
        $this->connectToDB();
        $this->displayData();
        
    }
    private function setData($latitude, $longitude, $radius){
        
        $this->latitude = $latitude;
        $this->longitude = $longitude;
        $this->radius = $radius;
    }
    private function connectToDB(){
        
        $this->connectToDatabase();
    }
    private function displayData(){
      
      $query = ("select enterprise_name,enterprise_address, latitude, longitude, ( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) * sin( radians( latitude ) ) ) ) AS distance FROM enterprise HAVING distance < ? ORDER BY distance LIMIT 0 , 20");
      $stmt = $this->con->prepare($query);
      $stmt->bind_param("dddd", $this->latitude, $this->longitude,  $this->latitude, $this->radius);
      $result = $stmt->execute();
      $stmt->bind_result($enterpriseName, $enterpriseAddress, $latitude, $longitude, $distance);
      if($result&& $stmt->fetch()){ 
         
         $response[] = array('enterpriseName'=>$enterpriseName,'enterpriseAddress'=>$enterpriseAddress,'latitude'=>$latitude,'longitude'=>$longitude,'distance'=>$distance); 
      while($stmt->fetch()){
          $response[] = array('enterpriseName'=>$enterpriseName,'enterpriseAddress'=>$enterpriseAddress,'latitude'=>$latitude,'longitude'=>$longitude,'distance'=>$distance);
         }
         echo '{"success":"1","enterprises":'.json_encode($response).'}';
      }
      else{
          echo '{"success":"0"}';
      }
      
  
  
  
  

		
        
    } 
    
}
