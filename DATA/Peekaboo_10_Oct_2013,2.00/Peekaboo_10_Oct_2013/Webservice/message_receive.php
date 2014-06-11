<?php
// http://192.168.1.196/PeekaBoo_Webservice/message_receive.php?uid=76
include('config.php');
	
	$uid = $_REQUEST['uid'];
	
	
	$sql = "SELECT * FROM messages WHERE receiver_uid=$uid ORDER BY id DESC";
	$result = mysql_query($sql) or die(mysql_error());
	$num_records = mysql_num_rows($result);
	$posts_image = array();	
	$posts_video = array();
	$posts = array();
	
	if($num_records > 0)	
	{
		$StatuS = true;
		while($row = mysql_fetch_array($result, MYSQL_ASSOC))
		{
			$isender_user_id = $row['isender_user_id'];
			
			$isender_Profile_Url_first = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/";
			$isender_Profile_Url =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['isender_Profile_Url'];
			
			if($isender_Profile_Url ==$isender_Profile_Url_first){
				$isender_Profile_Url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
			}					
			//$ProfileUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['User_Profile_Url'];
			$CurrTime = $row['iSending_date'];
			$sql2 = "SELECT user_name FROM users WHERE user_id='".$isender_user_id."'";
			$result2 = mysql_query($sql2) or die(mysql_error());
			$num_records2 = mysql_num_rows($result2);
			
			while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
			{
				$ImageUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['image_url'];
				//$ProfileUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['User_Profile_Url'];
				$iSenderName = $row2['user_name'];
				$posts_image[] = array('ProfileName'=>$iSenderName,'ProfilePic'=>$isender_Profile_Url,'MediaUrl'=>$ImageUrl,"CurrTime"=>$CurrTime);
			}
		}
		/** Code to add video url**/
		$sql_video = "SELECT * FROM message_video WHERE receiver_uid=$uid ORDER BY id DESC";
		$result_video = mysql_query($sql_video) or die(mysql_error());
		$num_records = mysql_num_rows($result_video);
		if($num_records > 0)	
		{
			while($row = mysql_fetch_array($result_video, MYSQL_ASSOC))
			{
				$vsender_user_id = $row['vsender_user_id'];
				
				$isender_Profile_Url_first = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/";
				$vsender_Profile_Url =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['vsender_Profile_Url'];
				
				if($vsender_Profile_Url ==$isender_Profile_Url_first){
					$vsender_Profile_Url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				}
				
				
				$msg_video_thumb =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['msg_video_thumb'];
				
				if($msg_video_thumb ==$isender_Profile_Url_first){
					$msg_video_thumb = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				}					
				//$ProfileUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['User_Profile_Url'];
				$CurrTime = $row['Sending_date'];
				
				$sql2 = "SELECT user_name FROM users WHERE user_id='".$vsender_user_id."'";
				$result2 = mysql_query($sql2) or die(mysql_error());
				$num_records2 = mysql_num_rows($result2);
				
				while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
				{
					$VideoUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['msg_video_url'];
					//$ProfileUrl = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row['User_Profile_Url'];
					$iSenderName = $row2['user_name'];
					$posts_video[] = array('ProfileName'=>$iSenderName,'ProfilePic'=>$vsender_Profile_Url,'MediaUrl'=>$msg_video_thumb,'VideoUrl'=>$VideoUrl,"CurrTime"=>$CurrTime);
				}
			}
			
		}
		$posts= array_merge($posts_image,$posts_video);
		foreach($posts as $val){
		   $res_array[$val['CurrTime']][] = $val;
		}
	krsort($res_array);
		array_reverse($res_array);		
		
		// echo "<pre>";
		// print_r($res_array);
		// echo "</pre>";
		$newArray = array();
		foreach($res_array as $key=>$val){
			foreach($val as $key=>$value){
				$newArray[] = $value;
			}
		}
		// echo "<pre>------------";
		// print_r($newArray);
		// echo "</pre>";
		$jk = array('status'=>$StatuS,'result'=>$newArray);
	}
	else
	{
		$StatuS = false;
		$jk = array('status'=>$StatuS,'result'=>"None of record.");
	}

	echo json_encode($jk);
?>