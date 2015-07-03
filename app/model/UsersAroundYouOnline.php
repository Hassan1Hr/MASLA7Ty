<?php
class UsersAroundYouOnline extends Database {
   private $latitude;
   private $longitude;
   
   private $filename = 'include/config.php';
    
   public function __construct($latitude, $longitude) {
       parent::__construct($this->filename);
       $this->setData($latitude, $longitude);
       $this->connectToDB();
       $this->getUsers();
       $this->con->close();
       
   } 
   private function setData($latitude, $longitude){
       $this->latitude = $latitude;
       $this->longitude = $longitude;
       
   }
   private function connectToDB() {
       $this->connectToDatabase();
   }
   private function getUsers() {
      $query = ("select firstName,lastName, latitude, longitude, ( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) * sin( radians( latitude ) ) ) ) AS distance FROM user HAVING distance < 25 ORDER BY distance LIMIT 0 , 20");
      $stmt = $this->con->prepare($query);
      $stmt->bind_param("ddd", $this->latitude, $this->longitude,  $this->latitude);
      $result = $stmt->execute();
      $stmt->bind_result($firstName, $lastName, $latitude, $longitude, $distance);
      if($result&&$stmt->fetch()){ 
         $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'latitude'=>$latitude,'longitude'=>$longitude,'distance'=>$distance); 
      while($stmt->fetch()){
          $response[] = array('firstName'=>$firstName,'lastName'=>$lastName,'latitude'=>$latitude,'longitude'=>$longitude,'distance'=>$distance); 
         }
         echo '{"success":"1","enterprises":'.json_encode($response).'}';
      }
      else{
          echo '{"success":"0"}';
      }
   }
   
}
