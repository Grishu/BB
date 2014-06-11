<?php
//http://192.168.1.196/PeekaBoo_Webservice/user.php?uid=76
include('config.php');
$uid = $_GET['uid'];
$sql = "SELECT user_name FROM users WHERE user_id =".$uid;
$result = mysql_query($sql,$conn) or die(mysql_error());
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records >= 1)
{
	$status= true;
$row = mysql_fetch_row($result);
$profilename = $row[0];

//echo $output . "<br>";

$sql2 = "SELECT follower_id FROM followers WHERE user_id=".$uid;
$result2 = mysql_query($sql2,$conn) or die(mysql_error());
$num_of_followers = mysql_num_rows($result2);
//echo $num_of_followers . "<br>";

$sql3 = "SELECT image_id,image_url FROM images WHERE user_id=".$uid." AND `profileflag` ='1'";
$result3 = mysql_query($sql3,$conn) or die(mysql_error());
$num_of_images = mysql_num_rows($result3);
//echo $num_of_images . "<br>";

while($row = mysql_fetch_array($result3, MYSQL_ASSOC))
   {
	    $imageurl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['image_url']; 
		//echo $imageurl . "<br>";
   }
   $posts[] = array('ProfileName'=>$profilename,'NumOfFollowers'=>$num_of_followers,'NumOfImages'=>$num_of_images,'ImageUrl'=>$imageurl);
   $jkkj = array('Status'=>$status,'result'=>$posts);
}
else
{
	$status= false;
	$jkkj = array('Status'=>$status,'result'=>"User Not Exists");
}
    echo json_encode(($jkkj));
?>