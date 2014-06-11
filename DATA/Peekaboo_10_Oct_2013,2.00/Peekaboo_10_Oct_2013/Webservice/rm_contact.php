<?php
//http://192.168.1.196/PeekaBoo_Webservice/rm_contact.php?uid=107&cid=98
include('config.php');
$uid = $_GET['uid'];
$cid = $_GET['cid'];
$sql = "SELECT * FROM contacts WHERE user_id=".$uid . " AND contact_id=".$cid;
$result = mysql_query($sql) or die(mysql_error());
$num_record = mysql_num_rows($result);
//echo $num_record;
$posts = array();
if($num_record <= 0)
{
	$Status = false;
	$posts = array('Status'=>$Status,'result'=>"No Record Here");
}
else
{
	$Status = true;
	$sql2 = "DELETE FROM contacts WHERE user_id=".$uid . " AND contact_id=".$cid;
	$result2 = mysql_query($sql2) or die(mysql_error());
	$posts = array('Status'=>$Status,'result'=>"Record Deleted Successfully");
}
echo json_encode(($posts));
?>