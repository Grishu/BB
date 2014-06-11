<?php
//http://192.168.1.196/PeekaBoo_Webservice/surprise.php?uid=77
include('config.php');
$uid = $_GET['uid'];

$delete = "delete from surprise";
			   
mysql_query($delete) or die(mysql_error());

$sql = "SELECT DISTINCT image_id FROM hearts";

$result = mysql_query($sql) or die(mysql_error());

while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
	 $hart_image_id = $row['image_id']; 
	 //$heartno = $row['num_of_hearts'];
	 
	 //echo $hart_image_id . "<br>";
	 
	 $sql2 = "SELECT num_of_hearts FROM hearts WHERE image_id=".$hart_image_id. " AND num_of_hearts=1";
	 
	 $result2 = mysql_query($sql2) or die(mysql_error());
	 
	 $totalheart = mysql_num_rows($result2);
	 
	 $no_of_visitors = $totalheart * 2;
	 
	 //echo $hart_image_id . "&nbsp;" . $totalheart . "&nbsp;" . $no_of_visitors . "<br>";
	 
	
			   
			   $sql4 = "SELECT b.user_name, c.image_url FROM users b, images c WHERE c.image_id =".$hart_image_id." AND b.user_id =c.user_id";
			   
			   $result4 = mysql_query($sql4) or die(mysql_error());
			     while($row4 = mysql_fetch_array($result4, MYSQL_ASSOC))
				 {
					 
					 //$Sfollower_id = $row4['follower_id'];
					 $Suser_name = $row4['user_name'];
					 $Simage_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row4['image_url'];
					
$sql3 = "SELECT  a.user_id FROM users a WHERE NOT EXISTS(SELECT b.follower_id FROM followers b WHERE a.user_id = b.user_id ) LIMIT ".$no_of_visitors."";
	 
$result3 = mysql_query($sql3) or die(mysql_error());

 while($row3 = mysql_fetch_array($result3, MYSQL_ASSOC))
		   {
			   $viewusersid = $row3['user_id'];
			   
               $sql5 = "INSERT INTO surprise (image_id,view_image_userid,foll_image_username,image_url) VALUES ('".$hart_image_id."','".$viewusersid."','".$Suser_name."','".$Simage_url."')";

$result5 = mysql_query($sql5) or die(mysql_error());

		   }
		   
		  
					 
				 }
				
		  
	 
}

 //New Code Start
 
/* $query = "SELECT DISTINCT video_id FROM videos_hearts";

$queryresult = mysql_query($query) or die(mysql_error());

while($vrow = mysql_fetch_array($queryresult, MYSQL_ASSOC))
{
	 $hart_video_id = $vrow['video_id']; 
	 //$heartno = $row['num_of_hearts'];
	 
	//echo $hart_video_id . "<br>";
	
	 $query2 = "SELECT num_videos_heart FROM videos_hearts WHERE video_id=".$hart_video_id. " AND num_videos_heart =1";
	 
	 $queryresult2 = mysql_query($query2) or die(mysql_error());
	 
	 $total_videos_heart = mysql_num_rows($queryresult2);
	 
	 $no_of_video_visitors = $total_videos_heart * 2;
	 
	 //echo $hart_video_id . "&nbsp;" . $total_videos_heart . "&nbsp;" . $no_of_video_visitors . "<br>";
	 
	  $query4 = "SELECT b.user_name, c.video_url FROM users b, videos c WHERE c.video_id =".$hart_video_id." AND b.user_id =c.user_id";
			   
			   $queryresult4 = mysql_query($query4) or die(mysql_error());
			     while($vrow4 = mysql_fetch_array($queryresult4, MYSQL_ASSOC))
				 {
					 
					 //$Sfollower_id = $row4['follower_id'];
					 $vuser_name = $vrow4['user_name'];
					 $vvideo_url = $vrow4['video_url'];
					 
					 //echo $vuser_name . "&nbsp;" . $video_url . "<br>";
					

 $query5 = "SELECT  a.user_id FROM users a WHERE NOT EXISTS(SELECT b.follower_id FROM followers b WHERE a.user_id = b.user_id ) LIMIT ".$no_of_video_visitors."";
	 
$queryresult5 = mysql_query($query5) or die(mysql_error());

 while($vrow5 = mysql_fetch_array($queryresult5, MYSQL_ASSOC))
		   {
			   $view_video_usersid = $vrow5['user_id'];
			  
			   
			   $query6 = "UPDATE surprise SET video_id=".$hart_video_id.", foll_video_username='".$vuser_name."', video_url='".$vvideo_url."' WHERE view_image_userid =".$view_video_usersid;

$queryresult6 = mysql_query($query6) or die(mysql_error());

		   }
 
		   
		  
					 
				 }
				
		  

			   
			 
	 
}*/

 // New Code End

$sql6 = "SELECT * FROM surprise WHERE view_image_userid =$uid";  
  $result6 = mysql_query($sql6) or die(mysql_error());
  
  $no_of_records = mysql_num_rows($result6);
  
 $posts = array();
  
   if($no_of_records > 0)
  
  {
  while($row6 = mysql_fetch_array($result6, MYSQL_ASSOC))
		   {
			   $status = true;
			   $image_id = $row6['image_id'];
			   $Imageprofile_name = $row6['foll_image_username'];
			   $image_url = $row6['image_url'];
			   $video_id = $row6['video_id'];
			   $Videoprofile_name = $row6['foll_video_username'];
			   $video_url = $row6['video_url'];
			   
			   
			  // echo $image_id . $profile_name . $image_url .  "<br>";
		
			  
			  $sql7 = "SELECT num_of_likes FROM likes WHERE image_id =".$image_id;
			  
			  $result7 = mysql_query($sql7) or die(mysql_error());
			  
			  $ImagelikesNum = mysql_num_rows($result7);
			  
			   $sql8 = "SELECT num_of_hearts FROM hearts WHERE image_id =".$image_id;
			  
			  $result8 = mysql_query($sql8) or die(mysql_error());
			  
			  $ImageheartNum = mysql_num_rows($result8);
			  
			   $sql9 = "SELECT num_videos_likes FROM videos_like WHERE video_id =".$video_id;
			  
			  $result9 = mysql_query($sql9) or die(mysql_error());
			  
			  $VideolikesNum = mysql_num_rows($result9);
			  
			   $sql21 = "SELECT num_videos_heart FROM videos_hearts WHERE video_id =".$video_id;
			  
			  $result21 = mysql_query($sql21) or die(mysql_error());
			  
			  $VideoheartNum = mysql_num_rows($result21);
			  
			  $sql22 = "SELECT user_id FROM users WHERE user_name='".$Imageprofile_name."'";
			  $result22 = mysql_query($sql22);
			  
			  $row22 = mysql_fetch_row($result22);
			  
			  $ProfileID = $row22[0];
			  
			 //echo $image_id . $profile_name . $image_url . $likesno . $heartno .  "<br>";
			  
$posts = array('ProfileID'=>$ProfileID,'Profile_Name'=>$Imageprofile_name,'ImageID'=>$image_id,'Image_url'=>$image_url,'Likes'=>$ImagelikesNum,'Hearts'=>$ImageheartNum);  
			   $abcd[] =array('result'=>$posts);
			   $janit = array('Status'=>$status,'result'=>$abcd);
			   
		   }
		   
		    //$abcd =array('Status'=>$status,'result'=>$posts);
			
			 
	   
	   
			
  }
  
  else
  {
	  $status = false;
	  
	  $janit =array('Status'=>$status,'result'=>"No Record Exist Here");
  }
		  echo json_encode(($janit));
   
?>