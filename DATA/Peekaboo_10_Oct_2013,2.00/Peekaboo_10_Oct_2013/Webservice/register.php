<?php 
//http://192.168.1.196/PeekaBoo_Webservice/register.php?uname=Sanjay&email=snv.jan@gmail.com&pass=admin123
include('config.php');
 $uname = $_REQUEST['uname'];
 $email = $_REQUEST['email'];
 $str = $_REQUEST['pass'];
 $description = "This is my Peeka\'Boo account. I post selfies here.";
 $pass = base64_encode($str);
 

	$fileName =$_FILES["file"]["name"];
    $fileType = $_FILES["file"]["type"];
    $fileSize =$_FILES["file"]["size"];
	$fileTmpLoc = $_FILES["file"]["tmp_name"];
    $fileErrorMsg = $_FILES["file"]["error"];
	$filename2 = $uname;
		
$sql="SELECT * FROM users WHERE user_name='$uname' OR user_email='$email'";

$result=mysql_query($sql,$conn) or die(mysql_error());

$posts = array();
$total_records = mysql_num_rows($result);



if($total_records < 1)
{
	$status= true;
	$insert="insert into users (user_name,user_email,user_pass,user_description)values('".$uname."','".$email."','".$pass."','".$description."')";
    mysql_query($insert,$conn) or die(mysql_error());	
	
	if($fileSize>0){
		$insertedID = mysql_insert_id();
		$ext = pathinfo($fileName, PATHINFO_EXTENSION);
		$newseldata= "select user_id from users where user_email='".$email."'";
		$newresult=mysql_query($newseldata,$conn) or die(mysql_error());
		$newrow = mysql_fetch_array($newresult);
		$newuserid = $newrow[0];
		$imagename = $newuserid;
		$lastimgname = $insertedID.".".$ext;
		$imageurl = "uploads/".$lastimgname;
		$imginsert = "insert into images (image_name,user_id,image_url,profileflag) values ('".$lastimgname."','".$insertedID."','".$imageurl."','1')";
		mysql_query($imginsert,$conn) or die(mysql_error());
		move_uploaded_file($fileTmpLoc, $imageurl);
	}		
	$posts = array('Status'=>$status,'result'=>"Registered Successfully");
}
else
{
	$status= false;
  
    $posts = array('Status'=>$status,'result'=>"User Already Exists");
}
echo json_encode(($posts));
?>
