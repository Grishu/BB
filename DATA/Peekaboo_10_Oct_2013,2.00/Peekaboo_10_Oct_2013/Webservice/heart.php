<?php
//http://192.168.1.196/PeekaBoo_Webservice/heart.php?uid=76&pid=552
include('config.php');
$uid = $_GET['uid'];
$pid = $_GET['pid'];

$sql = "select * from hearts where user_id=".$uid." AND image_id=".$pid;
$result=mysql_query($sql,$conn) or die(mysql_error());
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records < 1)
{
	$status= true;
	
	$i = $total_records + 1;
	$insert="insert into hearts (user_id,image_id,num_of_hearts)values('".$uid."','".$pid."','".$i."')";
    mysql_query($insert,$conn) or die(mysql_error());
	
	 $sql4 = "SELECT num_of_hearts FROM hearts WHERE image_id=".$pid." AND num_of_hearts=1";
		 $result4 = mysql_query($sql4) or die(mysql_error());
		 $num_img_heart = mysql_num_rows($result4);
	$posts = array('Status'=>$status,'Hearts'=>$num_img_heart);
}
else
{
	$status= true;
	$sql2 = "select * from hearts where user_id=".$uid." AND image_id=".$pid;
    $result2=mysql_query($sql,$conn) or die(mysql_error());
	$fetch2 = mysql_fetch_row($result2, MYSQL_ASSOC);
	$no_of_hearts = $fetch2['num_of_hearts'];
	
	   if($no_of_hearts == 0)
	   {
		    $i = 1;
	   }
	   else
	   {
		   $i = 0;
	   }
	
	//echo $i;
	$delete = "DELETE FROM hearts WHERE user_id=".$uid." AND image_id=".$pid;
	 mysql_query($delete,$conn) or die(mysql_error());
	
	$insertnew= "insert into hearts (user_id,image_id,num_of_hearts)values('".$uid."','".$pid."','".$i."')";
	
	mysql_query($insertnew,$conn) or die(mysql_error());
	
	$sql5 = "SELECT num_of_hearts FROM hearts WHERE image_id=".$pid." AND num_of_hearts=1";
		 $result5 = mysql_query($sql5) or die(mysql_error());
		 $num_img_heart = mysql_num_rows($result5);
  
    $posts = array('Status'=>$status,'Hearts'=>$num_img_heart);
}
echo json_encode(($posts));

?>