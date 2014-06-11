<?php
// http://192.168.1.196/PeekaBoo_Webservice/update_user_desc.php?uid=74&desc=hello

include('config.php');
	
$uid = $_GET['uid'];
$desc = $_GET['desc'];
$StatuS = true;

$sql = "UPDATE users SET user_description='".$desc."' WHERE user_id = $uid";
$result = mysql_query($sql) or die(mysql_error());
$posts = array('status'=>$StatuS,'result'=>"Update Succesfully");
echo json_encode($posts);
?>