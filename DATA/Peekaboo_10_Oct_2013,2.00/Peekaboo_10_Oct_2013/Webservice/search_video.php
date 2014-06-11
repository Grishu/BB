<?php

// http://192.168.1.196/PeekaBoo_Webservice/search_video.php?q=snv
include('config.php');
$like = $_REQUEST['q'];

$select = "select * from videos_hashtag where tag like '$like'";
$res = mysql_query($select);
$num = mysql_num_rows($res);

if($num>0){
	$status = true;
	while($row = mysql_fetch_assoc($res)){
		$profile_pic = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['video_url'];
		$result[] = array("Name"=>$row['video_name'],"MediaUrl"=>$profile_pic);
	}
}
else{
	$status = false;
	$result = "No records found";
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>