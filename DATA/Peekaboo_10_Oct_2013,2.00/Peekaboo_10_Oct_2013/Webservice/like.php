<?php
//http://192.168.1.196/PeekaBoo_Webservice/like.php?uid=76&pid=552
include('config.php');
$uid = $_GET['uid'];
$pid = $_GET['pid'];

$sql = "select * from likes where user_id=".$uid." AND image_id=".$pid;
$result=mysql_query($sql,$conn) or die(mysql_error());
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records < 1)
{
	$status= true;
	
	$i = $total_records + 1;
	$insert="insert into likes (user_id,image_id,num_of_likes)values('".$uid."','".$pid."','".$i."')";
    mysql_query($insert,$conn) or die(mysql_error());
	
	$sql3 = "SELECT num_of_likes FROM likes WHERE image_id=".$pid." AND num_of_likes=1";
		 $result3 = mysql_query($sql3) or die(mysql_error());
		 $num_img_likes = mysql_num_rows($result3);
	$posts = array('Status'=>$status,'Likes'=>$num_img_likes);
}
else
{
	$status= true;
	$sql2 = "select * from likes where user_id=".$uid." AND image_id=".$pid;
    $result2=mysql_query($sql,$conn) or die(mysql_error());
	$fetch2 = mysql_fetch_row($result2, MYSQL_ASSOC);
	$no_of_likes = $fetch2['num_of_likes'];
	
	   if($no_of_likes == 0)
	   {
		    $i = 1;
	   }
	   else
	   {
		   $i = 0;
	   }
	
	//echo $i;
	$delete = "DELETE FROM likes WHERE user_id=".$uid." AND image_id=".$pid;
	 mysql_query($delete,$conn) or die(mysql_error());
	
	$insertnew= "insert into likes (user_id,image_id,num_of_likes)values('".$uid."','".$pid."','".$i."')";
	
	mysql_query($insertnew,$conn) or die(mysql_error());
	
	$sql4 = "SELECT num_of_likes FROM likes WHERE image_id=".$pid." AND num_of_likes=1";
		 $result4 = mysql_query($sql4) or die(mysql_error());
		 $num_img_likes = mysql_num_rows($result4);
  
    $posts = array('Status'=>$status,'Likes'=>$num_img_likes);
}
echo json_encode(($posts));

?>