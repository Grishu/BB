<?php 
//http://192.168.1.196/PeekaBoo_Webservice/insertimage.php?uid=76&tag=janit,mohit,a,b
	
	include('config.php');
	
	$uid = $_REQUEST['uid'];
	
	//$uid = 74;
	if(isset($_REQUEST['tag']))
	{
		
		$arr = $_REQUEST['tag'];
		$tag = explode(',',$arr);
	}
	else
	{
		//$tag = 'janit';
	}

	$fileName = $_FILES["file"]["name"];				 
	$fileTmpLoc =$_FILES["file"]["tmp_name"];
	$fileSize = $_FILES["file"]["size"];
	// $fileType = $_FILES["file"]["type"];
	
	$date = date("Y-m-d H:i:s");
		
	$sql="select user_id,image_name from images where user_id='$uid'";

	$result=mysql_query($sql,$conn) or die(mysql_error());

	$posts = array();
	$total_records = mysql_num_rows($result);

	if($total_records>0){
		$count =  $total_records+1;
	}else{
		$count = 1;
	}
	
	if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		
		$lastimgname = $uid."_".$count.".".$ext;
		$imageurl = "uploads/".$lastimgname;
		$imginsert = "insert into `images` (`image_name`,`user_id`,`image_url`,`curr_timestemp`) values ('".$lastimgname."','".$uid."','".$imageurl."','".$date."')";
		mysql_query($imginsert,$conn) or die(mysql_error());
		move_uploaded_file($fileTmpLoc, $imageurl);
		$inserted = mysql_insert_id();
		
		//$sql2 = "SELECT * FROM hashtag WHERE image_name='".$lastimgname."' AND user_id=$uid AND tag='".$tag."'";
		//$result2 = mysql_query($sql2);
		//$num_result = mysql_num_rows($result2);
		
		if(isset($_REQUEST['tag']))
		{
			 $i = 0;
             while ($i < count($tag)) {
				 
				 $sql3 = "INSERT INTO hashtag (image_name,user_id,image_url,tag,image_id) values ('".$lastimgname."','".$uid."','".$imageurl."','".$tag[$i]."','".$inserted ."')";
				 $result3 = mysql_query($sql3);
				 
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