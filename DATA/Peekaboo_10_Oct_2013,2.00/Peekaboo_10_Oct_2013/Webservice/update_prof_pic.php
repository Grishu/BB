<?php
// http://192.168.1.196/PeekaBoo_Webservice/update_prof_pic.php?uid=74
include('config.php');
	
$uid = $_POST['uid'];

//$uid = 74;

$fileName = $_FILES["file"]["name"];				 
$fileTmpLoc =$_FILES["file"]["tmp_name"];
$fileSize = $_FILES["file"]["size"];

$sql = "SELECT * FROM `images` WHERE `user_id` =$uid AND `profileflag` = '1'";
$result = mysql_query($sql) or die(mysql_error());

$num_of_results = mysql_num_rows($result);

//echo $num_of_results; exit();
$posts =array();
if($num_of_results >=1)
{

if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		$count = 1;
		$lastimgname = $uid . "." . $ext;
		$imageurl = "uploads/".$lastimgname;
		//$imginsert = "insert into images (image_name,user_id,image_url) values ('".$lastimgname."','".$uid."','".$imageurl."')";
		
		$imgupdate = "UPDATE images SET image_url='".$imageurl."' WHERE user_id=$uid AND `profileflag` = '1'";
		mysql_query($imgupdate,$conn) or die(mysql_error());
		move_uploaded_file($fileTmpLoc, $imageurl);
		
		$StatuS = true;		
		$posts = array('Status'=>$StatuS,'result'=>'Update Successfullly');
}

}
else
{
	
	//$StatuS = false;		
	//$posts = array('Status'=>$StatuS,'result'=>'Not Update');
	
	if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		$count = 1;
		$lastimgname = $uid . "." . $ext;
		$imageurl = "uploads/".$lastimgname;
		//$imginsert = "insert into images (image_name,user_id,image_url) values ('".$lastimgname."','".$uid."','".$imageurl."')";
		
		
		
		$imginsert = "INSERT INTO images(user_id,image_name,image_url,profileflag) VALUES ($uid,'".$lastimgname."','".$imageurl."','1')";
		mysql_query($imginsert,$conn) or die(mysql_error());
		move_uploaded_file($fileTmpLoc, $imageurl);
		
		$StatuS = true;		
		$posts = array('Status'=>$StatuS,'result'=>'Update Successfullly');
}
}

echo json_encode($posts);
?>