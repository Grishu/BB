<?php
// http://192.168.1.196/PeekaBoo_Webservice/videoinsert.php?uid=76&tag=sanjay,atul,mahesh
include('config.php');
$uid = $_REQUEST['uid'];

	if(isset($_REQUEST['tag'])){
		$tag =  explode(",",$_REQUEST['tag']);
	}else{}

	//$uid = 76;
	$sql = "SELECT uuid( )";
	$result = mysql_query($sql);
	$row = mysql_fetch_row($result);
	$uuid = $row[0];
	
	$ffmpeg = "c:\\ffmpeg\\bin\\ffmpeg";

	$videoFile = $_FILES['file']['tmp_name'];


	$fileName = $_FILES['file']['name'];
	$fileTmpLoc =$_FILES["file"]["tmp_name"];
	$fileSize = $_FILES["file"]["size"];
	$file = current(explode(".", $fileName));
	
	$jk = "videos/VideoThumb/" . $uuid . '.jpg';
	//echo $jk;

	$size = "120*90";
	$getFromSecond = 1;

	$cmd = "$ffmpeg -i $videoFile -an -ss $getFromSecond -s $size $jk";
	shell_exec($cmd);

	$date = date("Y-m-d H:i:s");

	if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		
		$lastvideoname = $uuid.".".$ext;
		$videourl = "videos/".$lastvideoname;
		$videoinsert = "insert into `videos` (`user_id`,`video_url`,`video_thumb`,`vcurr_timestemp`) values ('".$uid."','".$videourl."','".$jk."','".$date."')";
		mysql_query($videoinsert,$conn) or die(mysql_error());
		move_uploaded_file($fileTmpLoc, $videourl);
		$inserted = mysql_insert_id();
	
		if(isset($_REQUEST['tag'])){
			$i = 0;
			while($i< count($tag)){
				$inset_tag = "insert INTO videos_hashtag(`video_name`,`user_id`,`video_url`,`tag`) VALUES('".$lastvideoname."' ,'".$uid."','".$videourl."','".$tag[$i]."')";
				$result_video = mysql_query($inset_tag);
				$i++;
			}
		}
		
	}else{
		$inserted = 0;
	}
	
	if($inserted){
		$status= true;
		$posts = array('Status'=>$status,'result'=>"Insert Successfully");
	}
	else
	{
		$status= false;  
		$posts = array('Status'=>$status,'result'=>"Not Uploaded");
	}
	echo json_encode(($posts));	
?>