<?php
//http://192.168.1.196/PeekaBoo_Webservice/videoheart.php?uid=76&vid=552
include('config.php');
$uid = $_GET['uid'];
$vid = $_GET['vid'];

$sql = "select * from videos_hearts where user_id=".$uid." AND video_id=".$vid;
$result=mysql_query($sql,$conn) or die(mysql_error());
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records < 1)
{
	$status= true;
	
	$i = $total_records + 1;
	$insert="insert into videos_hearts (user_id,video_id,num_videos_heart)values('".$uid."','".$vid."','".$i."')";
    mysql_query($insert,$conn) or die(mysql_error());
	
	$sql6 = "SELECT num_videos_heart FROM videos_hearts WHERE video_id=".$vid." AND num_videos_heart=1";
		 $result6 = mysql_query($sql6) or die(mysql_error());
		 $num_video_heart = mysql_num_rows($result6);
	$posts = array('Status'=>$status,'Hearts'=>$num_video_heart);
}
else
{
	$status= true;
	$sql2 = "select * from videos_hearts where user_id=".$uid." AND video_id=".$vid;
    $result2=mysql_query($sql,$conn) or die(mysql_error());
	$fetch2 = mysql_fetch_row($result2, MYSQL_ASSOC);
	$no_of_hearts = $fetch2['num_videos_heart'];
	
	   if($no_of_hearts == 0)
	   {
		    $i = 1;
	   }
	   else
	   {
		   $i = 0;
	   }
	
	//echo $i;
	$delete = "DELETE FROM videos_hearts WHERE user_id=".$uid." AND video_id=".$vid;
	 mysql_query($delete,$conn) or die(mysql_error());
	
	$insertnew= "insert into videos_hearts (user_id,video_id,num_videos_heart)values('".$uid."','".$vid."','".$i."')";
	
	mysql_query($insertnew,$conn) or die(mysql_error());
	
	$sql7 = "SELECT num_videos_heart FROM videos_hearts WHERE video_id=".$vid." AND num_videos_heart=1";
		 $result7 = mysql_query($sql7) or die(mysql_error());
		 $num_video_heart = mysql_num_rows($result7);
  
    $posts = array('Status'=>$status,'Hearts'=>$num_video_heart);
}
echo json_encode(($posts));

?>