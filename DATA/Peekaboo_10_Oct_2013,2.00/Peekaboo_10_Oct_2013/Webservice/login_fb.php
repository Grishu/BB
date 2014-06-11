<?php
//http://192.168.1.196/PeekaBoo_Webservice/login_fb.php?fbuid=100006607508262
include('config.php');

if(isset($_REQUEST['fbuid'])){
	$fbuid = $_REQUEST['fbuid'];
	$sql="select * from users where fbuid='$fbuid'";
}

$result=mysql_query($sql,$conn) or die(mysql_error());

$row = mysql_fetch_assoc($result, MYSQL_ASSOC);
	
$posts = array();
$total_records = mysql_num_rows($result);
if($total_records>0){
	$status= true;
	$user_name = $row['user_name'];
	$user_id = $row['user_id'];
	$abcd = array('user_id'=>$user_id,'user_name'=>$user_name);	
    $posts = array('Status'=>$status,'result'=>$abcd);
}
else
{
	$status= false;  
	$posts = array('Status'=>$status,'result'=>"Invalid UserName or Password");          
}
echo json_encode(($posts));

?>