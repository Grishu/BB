<?php /* Email List - send Inviation and Mail*/

// http://192.168.1.196/PeekaBoo_Webservice/email_list.php?email=snv.jan@gmail.com,test@testing.com,snv_3@rediffmail.com&uid=178
include('config.php');

$uid = $_REQUEST['uid'];
$email_list = explode(",",$_REQUEST['email']);
//print_r($email_list);

$select_main_user = "select * from users where user_id=$uid";
$result_main_user = mysql_query($select_main_user);
$row_main_user    = mysql_fetch_assoc($result_main_user);
$main_user_name = $row_main_user['user_name'];
$main_user_email= $row_main_user['email_name'];
$main_user_alter_email= $row_main_user['alternate_name'];

	foreach($email_list as $email){
		$select = "select user_email,alternate_email,user_id from users where user_email='$email' OR alternate_email='$email'";
		$result = mysql_query($select);
		$num = mysql_num_rows($result);
		
		if($num>0){
			$row = mysql_fetch_assoc($result);
			$user_id =  $row['user_id'];
			
			$select_user_contact = "select contact_id from contacts where user_id=$uid and contact_id=$user_id";
			$result_user_contact = mysql_query($select_user_contact);
			$num_user_contact    = mysql_num_rows($result_user_contact);
			if($num_user_contact<=0){
				$insert = "INSERT INTO contacts (`contact_id`,`user_id`,`contact_flag`) VALUES('".$user_id."','".$uid."','2')";
				mysql_query($insert);
			}
			
		}else{
			$server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
			$domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
			$server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
			
			$email = "janitkumarjoshi@gmail.com";
			
			$to = $email;
			$subject = "PeekaBoo: Invitation from $main_user_name";
			
			$headers = "From: PeekaBoo <".$server_email.">\r\n";
			$headers .= "Content-type: text/html\r\n";
			$message = "Hello \n";
			$message .= $main_user_name." send you a friend Request \n";
			$message .= '<a href="javascript">Accept It</a>';
			
			mail($to, $subject, $message, $headers) or die(mysql_error());
		}
	}


?>