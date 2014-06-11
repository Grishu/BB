<?php

// http://192.168.1.196/PeekaBoo_Webservice/search_user.php?q=snv
include('config.php');
$like = $_REQUEST['q'];

$select = "select * from users where user_name like '%$like%' OR alternate_email like '%$like%' OR user_email like '%$like%'";
$res = mysql_query($select);
$num = mysql_num_rows($res);

if($num>0){
	$status = true;
	while($row = mysql_fetch_assoc($res)){
		$user_id = $row['user_id'];
		$username = $row['user_name'];
		
		$select_image = "select image_url from images where user_id=$user_id and profileflag='1'";
		$res_image = mysql_query($select_image);
		$num_images = mysql_num_rows($res_image);
		if($num_images>0){
			$row_images = mysql_fetch_assoc($res_image);
			$profile_pic = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row_images['image_url'];
		}else{
			$profile_pic = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
		}
		$result[] = array("UserId"=>$user_id,"Name"=>$username,"MediaUrl"=>$profile_pic);
	}
}
else{
	$status = false;
	$result = "No records found";
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>