<?php
//http://192.168.1.196/PeekaBoo_Webservice/contact_selection.php?uid=74&cid=77
include('config.php');
$uid = $_GET['uid'];
$cid = $_GET['cid'];
$sql = "SELECT * FROM contacts WHERE user_id=$uid AND contact_id=$cid AND contact_flag = '0'";
$result = mysql_query($sql) or die(mysql_error());
$num_records = mysql_num_rows($result);
if($num_records >= 1)
{
	$StatuS = true;
	$update = "UPDATE contacts SET contact_flag = '1' WHERE user_id=$uid AND contact_id=$cid";
	$updateqry = mysql_query($update) or die(mysql_error());
	$posts = array('status'=>$StatuS,'result'=>"Contact added Successfully");
}
else
{
	$StatuS = false;
	$posts = array('status'=>$StatuS,'result'=>"Contact Not added");
}
echo json_encode($posts);
?>
