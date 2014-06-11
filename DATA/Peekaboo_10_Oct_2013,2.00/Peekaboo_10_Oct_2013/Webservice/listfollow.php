<?php
//http://192.168.1.196/PeekaBoo_Webservice/listfollow.php?uid=76
include('config.php');
$uid = $_GET['uid'];
$sql = "SELECT * FROM followers WHERE user_id=$uid";
$result = mysql_query($sql) or die(mysql_error());
$no_of_records = mysql_num_rows($result);
$posts = array();
  if($no_of_records > 0){
while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
	$Status = true;
	$follower_id = $row['follower_id'];
	
	//echo $follower_id . "<br>";
	$sql2 = "SELECT a.user_name, b.image_url FROM users a, images b WHERE a.user_id=$follower_id AND b.user_id=$follower_id AND b.profileflag='1'";
	$result2 = mysql_query($sql2) or die(mysql_error());
	$num = mysql_num_rows($result2);
	
	

	
		if($num < 1)
		{
			$sqlj = "SELECT user_name FROM users WHERE user_id=$follower_id";
			$resultj = mysql_query($sqlj);
			while($rowj = mysql_fetch_array($resultj, MYSQL_ASSOC))
			{
				
				$follower_name = $rowj['user_name'];
				$follower_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				$posts[] = array('ID'=>$follower_id,'ProfileName'=>$follower_name,'ImageUrl'=>$follower_image);
		        $janit = array('Status'=>$Status,'result'=>$posts);
			}
		}
		else
		{
			
	while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
	{
		$follower_name = $row2['user_name'];
		$follower_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
	
		//echo $follower_name . "<br>" . $follower_image;
		$posts[] = array('ID'=>$follower_id,'ProfileName'=>$follower_name,'ImageUrl'=>$follower_image);
		$janit = array('Status'=>$Status,'result'=>$posts);
	}
		}
}
  }
  else
  {
	  $Status = false;
	  $janit = array('Status'=>$Status,'result'=>"No Record Exist Here");
  }
   echo json_encode(($janit));
?>