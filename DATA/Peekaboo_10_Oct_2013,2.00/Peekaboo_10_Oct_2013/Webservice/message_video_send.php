<?php
// http://192.168.1.196/PeekaBoo_Webservice/message_video_send.php?uid=75&receiver_uid=76,72,73,74

include('config.php');

$sqlnew = "SELECT uuid( )";
	$resultnew = mysql_query($sqlnew);
	$rownew = mysql_fetch_row($resultnew);
	$uuid = $rownew[0];
	
	$uid = $_POST['uid'];
	
	$sqlstart = "SELECT image_url FROM images WHERE user_id=$uid AND profileflag='1'";
	$resultstart = mysql_query($sqlstart) or die(mysql_error());
	$num_record = mysql_num_rows($resultstart);
	
	$row = mysql_fetch_row($resultstart);
	$ProfileUrl = $row[0];
	
	//$uid = 76;
		
	if(isset($_REQUEST['receiver_uid']))
	{
		
		$arr = $_REQUEST['receiver_uid'];
		$receiver_uid = explode(',',$arr);
	}
	else
	{
	$receiver_uid = 'janit';
	}

	$fileName = $_FILES["file"]["name"];				 
	$fileTmpLoc =$_FILES["file"]["tmp_name"];
	$fileSize = $_FILES["file"]["size"];
	
	     $ffmpeg = "c:\\ffmpeg\\bin\\ffmpeg";
         $videoFile = $_FILES['file']['tmp_name'];
         $fileName = $_FILES['file']['name'];
         $fileTmpLoc =$_FILES["file"]["tmp_name"];
         $fileSize = $_FILES["file"]["size"];
         $file = current(explode(".", $fileName));
         $jk = "videos/VideoThumb/msgvthumb/" . $uuid . '.jpg';
         //echo $jk;
         $size = "120*90";
         $getFromSecond = 1;
         $cmd = "$ffmpeg -i $videoFile -an -ss $getFromSecond -s $size $jk";
         shell_exec($cmd);
	
	if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		
		$lastimgname = $uuid.".".$ext;
		$videourl = "uploads/msgmedia/".$lastimgname;
		move_uploaded_file($fileTmpLoc, $videourl);
		$inserted = mysql_insert_id();		
		
		
			 $i = 0;
             while ($i < count($receiver_uid)) {
				 
				 $sql = "INSERT INTO message_video (msg_video_name,vsender_user_id,vsender_Profile_Url,msg_video_url,receiver_uid,msg_video_thumb) values ('".$lastimgname."','".$uid."','".$ProfileUrl."','".$videourl."','".$receiver_uid[$i]."','".$jk."')";
				 $result = mysql_query($sql);
				 
				  $i++;
			 }
			 $StatuS = true;
			 $posts = array('status'=>$StatuS,'result'=>"Message Send Successully.");
}
else
{
	$StatuS = false;
	$posts = array('status'=>$StatuS,'result'=>"Message Sending failed.");
}
echo json_encode($posts);
?>