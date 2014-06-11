<?php 
//http://192.168.1.196/PeekaBoo_Webservice/myaccount_edit.php?uid=105&uname=Sanjay Vaghela&phone=9428165978&user_desc=this is testing&alternate_email=snv_3@gmail.com
	
	include('config.php');
	
	$uid = $_REQUEST['uid'];	
	
	$select = "select * from users where user_id=$uid";
	$result = mysql_query($select);
	$num = mysql_num_rows($result);
	if($num>0){
		$status= true;
		$row = mysql_fetch_assoc($result);
		
		if(isset($_REQUEST['uname']))
			$username = $_REQUEST['uname'];
		else		
			$username = $row['user_name'];
			
		if(isset($_REQUEST['user_desc']))
			$user_description = $_REQUEST['user_desc'];
		else
			$user_description = $row['user_description'];
		
		
		if(isset($_REQUEST['phone']))
			$phone = $_REQUEST['phone'];
		else
			$phone = $row['phone'];
					
		if(isset($_REQUEST['alternate_email']))
			$alternate_email = $_REQUEST['alternate_email'];
		else
			$alternate_email = $row['alternate_email'];
			
		$update = "UPDATE users set user_name='$username',user_description='$user_description',phone='$phone',alternate_email='$alternate_email' where user_id='".$uid."'";
		mysql_query($update);
		
		$posts = array('Status'=>$status,'result'=>"Record Updated Successfully");			
	}
	else{
		$status= false;
		$posts = array('Status'=>$status,'result'=>"No Record Found");
	}
	echo json_encode(($posts));
	
?>