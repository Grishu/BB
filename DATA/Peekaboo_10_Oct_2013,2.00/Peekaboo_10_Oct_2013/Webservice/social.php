<?php
//http://192.168.1.196/PeekaBoo_Webservice/social.php?uid=76
include('config.php');
$uid = $_GET['uid'];

$path = dirname(__FILE__) ;

$host = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/";

$sql = "SELECT user_name,user_description FROM users WHERE user_id =".$uid;
$result = mysql_query($sql,$conn) or die(mysql_error());

$posts = array();

$total_records = mysql_num_rows($result);

if($total_records >= 1)
{
	$status= true;
	$row = mysql_fetch_assoc($result);
	$profilename = $row['user_name'];
	$description = $row['user_description'];

	$followers_query = "SELECT follower_id FROM followers WHERE user_id=".$uid;
	$result2 = mysql_query($followers_query,$conn) or die(mysql_error());
	$num_of_followers = mysql_num_rows($result2);
	
	$images_query = "SELECT image_id,image_url FROM images WHERE user_id=".$uid;
	$result3 = mysql_query($images_query,$conn) or die(mysql_error());
	$num_of_images = mysql_num_rows($result3);
		
  if($num_of_images>0)
{
	while($row = mysql_fetch_array($result3, MYSQL_ASSOC))
	{
	    //if(realpath($path."/".$row['image_url']) && file_get_contents($host.$row['image_url'])!=""){
		if(file_exists(realpath($path."/".$row['image_url']))){
		$imageurl[] = $host.$row['image_url'];
		}
		else
		{
			$imageurl = "NoImage";
		}
	}
}
else
{
	$imageurl = "NoImage";
}
	$profile_image_query = "SELECT image_id,image_url FROM images WHERE user_id=".$uid." AND profileflag='1'";
	$result4 = mysql_query($profile_image_query,$conn) or die(mysql_error());
	$profleimages_count = mysql_num_rows($result4);
	if($profleimages_count>0)
	{
		while($rowsget = mysql_fetch_assoc($result4)){

			//if(realpath($path."/".$rowsget['image_url']) && file_get_contents($host.$rowsget['image_url'])!=""){
			if(file_exists(realpath($path."/".$rowsget['image_url']))){	
			$profleimage = $host.$rowsget['image_url'];
			}else{
				$profleimage ="NoImage";
			}
			
		}
	}else{
		$profleimage ="NoImage";
	}
	
	$contact_query = "SELECT contact_id FROM contacts WHERE user_id=".$uid." and contact_flag='1'"; //ddd
	$result5 = mysql_query($contact_query,$conn) or die(mysql_error());
	$num_of_contacts = mysql_num_rows($result5);
	
	$posts[] = array('ID'=>$uid,'ProfileName'=>$profilename,'Description'=>$description,'ProfileImage'=>$profleimage,'NumOfContact'=>$num_of_contacts,'NumOfFollowers'=>$num_of_followers,'NumOfImages'=>$num_of_images,'ImageUrl'=>$imageurl);
	$jkkj = array('Status'=>$status,'result'=>$posts);
}
else
{
	$status= false;
	$jkkj = array('Status'=>$status,'result'=>"User Not Exists");
}
    echo json_encode(($jkkj));
?>