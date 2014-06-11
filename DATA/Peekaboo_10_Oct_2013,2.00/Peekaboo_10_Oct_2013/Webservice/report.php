<?php
// http://192.168.1.196/PeekaBoo_Webservice/report.php?uid=76&imgid=27
include('config.php');
$uid = $_GET['uid'];
$imgid = $_GET['imgid'];

//echo $uid . "<br>" . $imgid;

$sql = "SELECT user_name,image_url FROM users,images WHERE users.user_id=$uid AND images.image_id=$imgid";
$result = mysql_query($sql) or die(mysql_error());
$num_records = mysql_num_rows($result);
if($num_records > 0)
{
$StatuS = true;
	 while($row = mysql_fetch_array($result, MYSQL_ASSOC))
	 {
		 $UserName = $row['user_name'];
		 $ImageUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['image_url'];
		 
		 //echo $UserName . "<br>" . $ImageUrl;
		 
		      $server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
			  $domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
			  $server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
			  
			  $email = "janitkumarjoshi@gmail.com";
			  
			  $to = $email;
			  $subject = "Image Report";
			  $headers = "From: Your Site <".$server_email.">\r\n";
			  $headers .= "Content-type: text/html\r\n";
			  $message = "User ".$UserName." reported you to an Image :-".$ImageUrl;
							  
				  
			  mail($to, $subject, $message, $headers) or die(mysql_error());
			  
			  $posts = array('status'=>$StatuS,'result'=>"Your Message Send Successfully");
			  
	 }
}
else
{
$StatuS = false;
 $posts = array('status'=>$StatuS,'result'=>"Your Message Sending Failed");
}
echo json_encode($posts);
?>