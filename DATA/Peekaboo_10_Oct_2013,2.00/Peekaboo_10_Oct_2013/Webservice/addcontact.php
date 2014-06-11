<?php
//http://192.168.1.196/PeekaBoo_Webservice/addcontact.php?uid=74&cid=77
include('config.php');
$uid = $_GET['uid'];
$cid = $_GET['cid'];
//echo $uid . $fwid;
$sql = "SELECT * FROM contacts WHERE user_id=".$uid . " AND contact_id=".$cid;
$result = mysql_query($sql) or die(mysql_error());
$num_record = mysql_num_rows($result);
//echo $num_record;
$posts = array();
if($num_record <= 0)
{
	$Status = true;
	$sql2 = "INSERT INTO contacts (user_id,contact_id,contact_flag) VALUES($uid,$cid,'0')";
	$result2 = mysql_query($sql2) or die(mysql_error());
	$posts = array('Status'=>$Status,'result'=>"Record Status is pending");
}
else
{
	$Status = false;
	$posts = array('Status'=>$Status,'result'=>"Record already Exists");
}
echo json_encode(($posts));
?>