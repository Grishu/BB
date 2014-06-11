<?php
//http://192.168.1.196/PeekaBoo_Webservice/videolike.php?uid=76&vid=552
include('config.php');
$uid = $_GET['uid'];
$vid = $_GET['vid'];

$sql = "select * from videos_like where user_id=".$uid." AND video_id=".$vid;
$result=mysql_query($sql,$conn) or die(mysql_error());
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records < 1)
{
	$status= true;
	
	$i = $total_records + 1;
	$insert="insert into videos_like (user_id,video_id,num_videos_likes)values('".$uid."','".$vid."','".$i."')";
    mysql_query($insert,$conn) or die(mysql_error());
	
	$sql5 = "SELECT num_videos_likes FROM videos_like WHERE video_id=".$vid." AND num_videos_likes=1";
		 $result5 = mysql_query($sql5) or die(mysql_error());
		 $num_video_likes = mysql_num_rows($result5);
	$posts = array('Status'=>$status,'Likes'=>$num_video_likes);
}
else
{
	$status= true;
	$sql2 = "select * from videos_like where user_id=".$uid." AND video_id=".$vid;
    $result2=mysql_query($sql,$conn) or die(mysql_error());
	$fetch2 = mysql_fetch_row($result2, MYSQL_ASSOC);
	$no_of_likes = $fetch2['num_videos_likes'];
	
	   if($no_of_likes == 0)
	   {
		    $i = 1;
	   }
	   else
	   {
		   $i = 0;
	   }
	
	//echo $i;
	$delete = "DELETE FROM videos_like WHERE user_id=".$uid." AND video_id=".$vid;
	 mysql_query($delete,$conn) or die(mysql_error());
	
	$insertnew= "insert into videos_like (user_id,video_id,num_videos_likes)values('".$uid."','".$vid."','".$i."')";
	
	mysql_query($insertnew,$conn) or die(mysql_error());
	
	$sql6 = "SELECT num_videos_likes FROM videos_like WHERE video_id=".$vid." AND num_videos_likes=1";
		 $result6 = mysql_query($sql6) or die(mysql_error());
		 $num_video_likes = mysql_num_rows($result6);
  
    $posts = array('Status'=>$status,'Likes'=>$num_video_likes);
}
echo json_encode(($posts));

?>