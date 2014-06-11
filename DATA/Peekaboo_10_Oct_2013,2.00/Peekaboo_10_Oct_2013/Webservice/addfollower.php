<?php
//http://192.168.1.196/PeekaBoo_Webservice/addfollower.php?uid=76&fwid=77
include('config.php');
$uid = $_GET['uid'];
$fwid = $_GET['fwid'];
//echo $uid . $fwid;
$sql = "SELECT * FROM followers WHERE user_id=".$uid . " AND follower_id=".$fwid;
$result = mysql_query($sql) or die(mysql_error());
$num_record = mysql_num_rows($result);
//echo $num_record;
$posts = array();
if($num_record <= 0)
{
	$Status = true;
	$sql2 = "INSERT INTO followers (user_id,follower_id) VALUES($uid,$fwid)";
	$result2 = mysql_query($sql2) or die(mysql_error());
	$posts = array('Status'=>$Status,'result'=>"Record Inserted Successfully");
}
else
{
	$Status = false;
	$posts = array('Status'=>$Status,'result'=>"Record already Exists");
}
echo json_encode(($posts));
?>