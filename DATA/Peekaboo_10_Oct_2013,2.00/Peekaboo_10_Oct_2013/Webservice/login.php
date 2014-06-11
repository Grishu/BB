<?php
//http://192.168.1.196/PeekaBoo_Webservice/login.php?userlogin=test@gmail.com&password=test1234
include('config.php');
$str = $_GET['password'];
$sql="select * from users where user_email='$_GET[userlogin]'";

$result=mysql_query($sql,$conn) or die(mysql_error());

$row = mysql_fetch_array($result, MYSQL_ASSOC);

	$str2  = $row['user_pass'];
	
	//echo $str . "<br>";
	 
	$pass = base64_decode($str2);
	
	//echo $pass;
	
$posts = array();
$total_records = mysql_num_rows($result);

if($str == $pass){
	$status= true;
	$user_id = $row['user_id'];
	$user_name = $row['user_name'];
	$user_email = $row['user_email'];
  $abcd = array('user_id'=>$user_id,'user_name'=>$user_name,'user_email'=>$user_email,'user_pass'=>$pass);
    $posts = array('Status'=>$status,'result'=>$abcd);
	
	
}
else
{
	$status= false;
  
   $posts = array('Status'=>$status,'result'=>"Invalid UserName or Password");
   
   
  
  
}
echo json_encode(($posts));

?>