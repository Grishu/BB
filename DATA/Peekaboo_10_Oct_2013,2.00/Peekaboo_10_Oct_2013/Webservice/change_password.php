<?php 
//http://192.168.1.196/PeekaBoo_Webservice/change_password.php?uid=105&password=test123
	
	include('config.php');
	
	$uid = $_REQUEST['uid'];
	
	$select = "select * from users where user_id=$uid";
	$result = mysql_query($select);
	$num = mysql_num_rows($result);
	if($num>0){
		$status= true;		
		$pass = base64_encode($_REQUEST['password']);
		$update = "UPDATE users set user_pass='".$pass."' where user_id='".$uid."'";
		mysql_query($update);
		
		$posts = array('Status'=>$status,'result'=>"Password Changed Successfully");
	}
	else{
		$status= false;
		$posts = array('Status'=>$status,'result'=>"No Record Found");
	}
	echo json_encode(($posts));
	
?>