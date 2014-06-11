<?php
//http://192.168.1.196/PeekaBoo_Webservice/list_all_contacts.php?uid=74
include('config.php');
$uid = $_GET['uid'];

$sql = "SELECT * FROM contacts WHERE user_id=$uid AND contact_flag = '0'";
$result = mysql_query($sql) or die(mysql_error());
$num_records = mysql_num_rows($result);
if($num_records > 0){
while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
	$Status = true;
	$contact_id = $row['contact_id'];
	
	//echo $follower_id . "<br>";
	$sql2 = "SELECT a.user_name, b.image_url FROM users a, images b WHERE a.user_id=$contact_id AND b.user_id=$contact_id and b.profileflag='1'";
	$result2 = mysql_query($sql2) or die(mysql_error());
	$num = mysql_num_rows($result2);
	if($num < 1)
		{
			$sqlj = "SELECT user_name FROM users WHERE user_id=$contact_id";
			$resultj = mysql_query($sqlj);
			while($rowj = mysql_fetch_array($resultj, MYSQL_ASSOC))
			{
				
				$contact_name = $rowj['user_name'];
				$contact_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				$posts[] = array('ID'=>$contact_id,'ProfileName'=>$contact_name,'ImageUrl'=>$contact_image);
		        $janit = array('Status'=>$Status,'result'=>$posts);
			}
		}
		else
		{
	while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
	{
		$contact_name = $row2['user_name'];
		$contact_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
		
		//echo $follower_name . "<br>" . $follower_image;
		$posts[] = array('ID'=>$contact_id,'ProfileName'=>$contact_name,'ImageUrl'=>$contact_image);
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