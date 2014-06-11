<?php
//http://192.168.1.196/PeekaBoo_Webservice/forgetpass.php?username=ironman
//http://192.168.1.196/PeekaBoo_Webservice/forgetpass.php?username=iron@gmail.com
include('config.php');
$posts = array();
if (isset($_REQUEST['username']))
{
	if (!isset($_REQUEST['email']))
	{
		//echo "username";
		$user_name = $_REQUEST['username'];
		$sql = "SELECT * FROM users WHERE user_name='".$user_name."' OR user_email='".$user_name."'";
		$result = mysql_query($sql,$conn) or die(mysql_error());
  
		$num_of_results = mysql_num_rows($result);
	  
		if($num_of_results > 0)
		  {
	         $Status = true;
	
	         $row = mysql_fetch_array($result, MYSQL_ASSOC);
	
			  $str = $row['user_pass'];
			  $user_pass = base64_decode($str);
			  $useremail = $row['user_email'];
		
		      //echo $user_pass;
			  $server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
			  $domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
			  $server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
			  
			  $email = $row['user_email'];
			  
			  $to = $email;
			  $subject = "Forgot Password";
			  $headers = "From: Your Site <".$server_email.">\r\n";
			  $headers .= "Content-type: text/html\r\n";
			  $message = "You have requested that you forgot your password1.<br>
							  Password: <b>".$user_pass."<b>";
				  
			  mail($to, $subject, $message, $headers) or die(mysql_error());
				  
			  
			  
			 $posts = array('Status'=>$Status,'result'=>"Password Sent to your Email.");
         }
       else
         {
		  $Status = false;
		  $posts = array('Status'=>$Status,'result'=>"User Not Registered");
		 
         }
	  
	}
 else
	   {
		   $user_name = $_REQUEST['username'];
		  $sql = "SELECT * FROM users WHERE user_name='".$user_name."' OR user_email='".$user_name."'";
           $result = mysql_query($sql,$conn) or die(mysql_error());

		   $num_of_results = mysql_num_rows($result);
		   $sql = "SELECT * FROM users WHERE user_name='".$user_name."'";
		   $result = mysql_query($sql,$conn) or die(mysql_error());

           $num_of_results = mysql_num_rows($result);
	  
	       if($num_of_results > 0)
               {
	             $Status = true;
	
	             $row = mysql_fetch_array($result, MYSQL_ASSOC);
	
				  $str = $row['user_pass'];
				  $user_pass = base64_decode($str);
				  $useremail = $row['user_email'];
		
				 //echo $user_pass;
				 $server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
				 $domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
				 $server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
			
				 $email = $row['user_email'];
			
				$to = $email;
				$subject = "Forgot Password";
				$headers = "From: Your Site <".$server_email.">\r\n";
				$headers .= "Content-type: text/html\r\n";
				$message = "You have requested that you forgot your password2.<br>
							Password: <b>".$user_pass."<b>";
				
				mail($to, $subject, $message, $headers) or die(mysql_error());
				
			
			
				$posts = array('Status'=>$Status,'result'=>"Password Sent to your Email.");
             }
           else
             {
				$Status = false;
				//echo "User&Email Not Registered";
				$posts = array('Status'=>$Status,'result'=>"User & Email Not Registered");
             }
	}
}

elseif (isset($_REQUEST['email']))
{
	if (!isset($_REQUEST['username']))
	{
	    //echo "email";
	   
	    $user_email = $_REQUEST['email'];
	   $sql = "SELECT * FROM users WHERE user_name='".$user_name."' OR user_email='".$user_name."'";
        $result = mysql_query($sql,$conn) or die(mysql_error());

        $num_of_results = mysql_num_rows($result);
	    if($num_of_results > 0)
         {
	       $Status = true;
	
	       $row = mysql_fetch_array($result, MYSQL_ASSOC);
	
		   $str = $row['user_pass'];
		   $user_pass = base64_decode($str);
		   $useremail = $row['user_email'];
		
		   //echo $user_pass;
		   $server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
		   $domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
		   $server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
	  
		   $email = $row['user_email'];
	  
		  $to = $email;
		  $subject = "Forgot Password";
		  $headers = "From: Your Site <".$server_email.">\r\n";
		  $headers .= "Content-type: text/html\r\n";
		  $message = "You have requested that you forgot your password3.<br>
					  Password: <b>".$user_pass."<b>";
		  
		  mail($to, $subject, $message, $headers) or die(mysql_error());
		  
	  
	  
		 $posts = array('Status'=>$Status,'result'=>"Password Sent to your Email.");
       }
      else
      {
		$Status = false;
		 //echo "Email Not Registered";
		 $posts = array('Status'=>$Status,'result'=>"Email Not Registered");
     }
}
	else
	{
		$user_email = $_REQUEST['email'];
	    $sql = "SELECT * FROM users WHERE user_name='".$user_name."' OR user_email='".$user_name."'";
        $result = mysql_query($sql,$conn) or die(mysql_error());

        $num_of_results = mysql_num_rows($result);
	    if($num_of_results > 0)
         {
	       $Status = true;
	
	       $row = mysql_fetch_array($result, MYSQL_ASSOC);
	
		   $str = $row['user_pass'];
		   $user_pass = base64_decode($str);
		   $useremail = $row['user_email'];
		
		   //echo $user_pass;
		   $server_un = 'admin'; //USERNAME OF YOUR WEBSITE EMAIL ADDRESS (CHANGE TO YOUR OWN) Ex: admin
		   $domain = $_SERVER["HTTP_HOST"]; //DOMAIN NAME WITH EXTENSION; Ex: (wcetdesigns.com)
		   $server_email = $server_un.'@'.$domain; //YOUR WEBSITE'S EMAIL ADDRESS
	  
		   $email = $row['user_email'];
	  
		  $to = $email;
		  $subject = "Forgot Password";
		  $headers = "From: Your Site <".$server_email.">\r\n";
		  $headers .= "Content-type: text/html\r\n";
		  $message = "You have requested that you forgot your password4.<br>
					  Password: <b>".$user_pass."<b>";
		  
		  mail($to, $subject, $message, $headers) or die(mysql_error());
		  
	  
	  
		  $posts = array('Status'=>$Status,'result'=>"Password Sent to your Email.");
       }
      else
      {
		$Status = false;
		// echo "User&Email Not Registered";
		 
		 $posts = array('Status'=>$Status,'result'=>"User & Email Not Registered");
     }
	}
}
else
{
	$posts = array('Status'=>$Status,'result'=>"User & Email Not Registered");
}
echo json_encode(($posts));
?>