<?php 
//http://192.168.1.196/PeekaBoo_Webservice/myaccount.php?uid=76
	
	include('config.php');
	
	$uid = $_REQUEST['uid'];
	
	$select = "select * from users where user_id=$uid";
	$result = mysql_query($select);
	$num = mysql_num_rows($result);
	if($num>0){
		$status= true;
		$row = mysql_fetch_assoc($result);
		$username = $row['user_name'];
		$user_email = $row['user_email'];
		//$user_description = $row['user_description'];
		$phone = $row['phone'];
		if(!$phone){
			$phone = "NoPhone";
		}
		$alternate_email = $row['alternate_email'];
		if(!$alternate_email){
			$alternate_email = "NoEmail";
		}
		
		// $select_profileimage = "select image_name,image_url from images where user_id=$uid and profileflag='1'";
		// $result_profileimage = mysql_query($select_profileimage);
		// $image_num = mysql_num_rows($result_profileimage);
		// if($image_num>0){
			// $image_row = mysql_fetch_assoc($result_profileimage);
			// $image_url = $image_row['image_url'];
		// }else{
			// $image_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
		// }
		
		// $select_images = "select image_name,image_url from images where user_id=$uid and profileflag='1'";
		// $result_images = mysql_query($select_images);
		// $images_num = mysql_num_rows($result_images);
		// if($images_num>0){
			// $images_row = mysql_fetch_assoc($result_images);
			// $images_url = $image_row['image_url'];
		// }else{
			// $images_url = "NoImages";
		// }
		
		// $select_video = "select video_url from videos where user_id=$uid";
		// $result_video = mysql_query($select_video);
		// $video_num = mysql_num_rows($result_video);
		// if($video_num>0){
			// $video_row = mysql_fetch_assoc($result_video);
			// $video_url = $image_row['image_url'];
		// }else{
			// $video = "NoImages";
		// }
		
		
		$return[] = array("UserName"=>$username,"UserEmail"=>$user_email,"Phone"=>$phone,"AlternateEmail"=>$alternate_email);
		
		
		
		
		$posts = array('Status'=>$status,'result'=>$return);			
	}
	else{
		$status= false;
		$posts = array('Status'=>$status,'result'=>"No Record Found");
	}
	echo json_encode(($posts));
	
?>