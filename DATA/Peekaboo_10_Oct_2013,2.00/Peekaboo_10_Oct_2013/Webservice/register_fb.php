<?php 
//http://192.168.1.196/PeekaBoo_Webservice/register_fb.php?uname=Sanjay&fbuid=100006607508262
include('config.php');	
	$uname = $_REQUEST['uname'];	
	$fbuid = $_REQUEST['fbuid'];
	
	$description = "This is my Peeka\'Boo account. I post selfies here.";
	
		
	$sql="SELECT * FROM users WHERE fbuid=$fbuid";

	$result=mysql_query($sql,$conn) or die(mysql_error());

	$posts = array();
	$total_records = mysql_num_rows($result);

	if($total_records < 1)
	{
		$status= true;
		$insert="insert into users (user_name,user_description,fbuid)values('".$uname."','".$description."','".$fbuid."')";
		mysql_query($insert,$conn) or die(mysql_error());	
		
		$insertedID = mysql_insert_id();
		
		$url = 'http://graph.facebook.com/'.$fbuid.'/picture';
		$data = file_get_contents($url);
		$fileName = $insertedID.'.jpg';
		$file = fopen($fileName, 'w+');
		fputs($file, $data);
		fclose($file);
		$uploads_dir = "uploads"; 
		 
		if (!copy($fileName, "$uploads_dir/$fileName")) {
			echo "failed to copy $file...\n";
		}
		unlink($fileName);
		$imageurl = "$uploads_dir/".$fileName;			
		
		$imginsert = "insert into images (image_name,user_id,image_url,profileflag) values ('".$fileName."','".$insertedID."','".$imageurl."','1')";
		mysql_query($imginsert,$conn) or die(mysql_error());		
	
	$posts = array('Status'=>$status,'result'=>"Registered Successfully");
}
else
{
	$status= false;
  
    $posts = array('Status'=>$status,'result'=>"User Already Exists");
}
echo json_encode(($posts));
?>
