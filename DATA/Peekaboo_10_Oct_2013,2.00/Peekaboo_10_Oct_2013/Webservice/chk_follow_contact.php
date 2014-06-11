<?php
//http://192.168.1.196/PeekaBoo_Webservice/chk_follow_contact.php?uid=76&fcid=77
include('config.php');
$uid = $_GET['uid'];
$fcid = $_GET['fcid'];
//echo $uid . "<br>" . $fwid;
$sql = "SELECT * FROM followers WHERE user_id=$uid AND follower_id=$fcid";
$result = mysql_query($sql) or die(mysql_error());
$num_of_result = mysql_num_rows($result);
//echo $num_of_result;
$posts = array();
$Status = true;
if($num_of_result > 0)
{
    $flag = 1;
}
else
{
	$flag = 0;
}
//echo "Follower" . $flag . "<br>";

$sql2 = "SELECT contact_flag FROM contacts WHERE user_id=$uid AND contact_id=$fcid";
$result2 = mysql_query($sql2) or die(mysql_error());
$num_of_result2 = mysql_num_rows($result2);

$row = mysql_fetch_row($result2);

$flag2 = $row[0];

if($flag2 == '0')
{
	$resultflag = 0;
}
elseif($flag2 == '1')
{
	$resultflag = 1;
}
else
{
	$resultflag = 2;
}

//echo "Contact" . $flag . "<br>";

$abcd = array('Followers'=>$flag, 'Contact'=>$resultflag);

$posts = array('Status'=>$Status,'result'=>$abcd);

echo json_encode($posts);
?>