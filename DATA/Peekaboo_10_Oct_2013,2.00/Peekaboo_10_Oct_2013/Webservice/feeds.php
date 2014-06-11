<?php 

//http://192.168.1.196/PeekaBoo_Webservice/feeds.php?uid=76&page=1
include('config.php');
$uid = $_GET['uid'];

$select_followers = "select * from followers where user_id=$uid";
$result = mysql_query($select_followers);
$num_rows = mysql_num_rows($result);


$no_of_records = mysql_num_rows($result);
if($no_of_records > 0)
{   /*Start first brace*/
	while($row = mysql_fetch_array($result, MYSQL_ASSOC))
	{   /*Start Second brace*/
		$status = true;
		$followerid = $row['follower_id']; 
	  
		$sqlname = "SELECT * FROM users WHERE user_id=$followerid";
		$resulname = mysql_query($sqlname) or die(mysql_error());
		while($rowname = mysql_fetch_array($resulname, MYSQL_ASSOC))
		{   /*Start Third brace*/
			$profilename = $rowname['user_name'];
			//echo $followerid . "<br>";
	 
			$sql2 = "SELECT * FROM images LEFT JOIN videos ON 0 WHERE images.user_id=$followerid  AND images.profileflag='0' 						
						UNION 						
					 SELECT * FROM images RIGHT JOIN videos ON 0 WHERE videos.user_id=$followerid";
						
			$result2 = mysql_query($sql2) or die(mysql_error());
			
			$num_total = mysql_num_rows($result2);
			if($num_total < 1)
			{
				/*Start BlankImage Code*/
				/*End BlankImage Code*/				
			}
			else
			{
				while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
				{
					$image_id =  $row2['image_id'];
					$ImageCurrTime = $row2['curr_timestemp'];
					$VideoCurrTime = $row2['vcurr_timestemp'];
					if($image_id =='')
					{
						$image_id = "noimage";
					}
					$image_urlfrst = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/";
					$image_url =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
					if($image_url ==$image_urlfrst)
					{
						$image_url = "noimageurl";
					}
					
					$video_id =  $row2['video_id'];
					if($video_id =='')
					{
						$video_id = "novideo";
						$video_url = "novideo";
					}
					
					$video_urlfrst = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/";
					$video_url =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['video_url'];
					$video_thumb_url =  "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['video_thumb'];
					
					if($video_url ==$video_urlfrst)
					{
						$video_url = "novideourl";
					}
					
					if($video_thumb_url ==$video_urlfrst)
					{
						$video_thumb_url = "novideothumb";
					}
		
					$sql3 = "SELECT num_of_likes FROM likes WHERE image_id='".$image_id."' AND num_of_likes=1";
					$result3 = mysql_query($sql3) or die(mysql_error());
					$num_img_likes = mysql_num_rows($result3); 
					//echo $num_img_likes . "<br>";
		
					$sql4 = "SELECT num_of_hearts FROM hearts WHERE image_id='".$image_id."' AND num_of_hearts=1";
					$result4 = mysql_query($sql4) or die(mysql_error());
					$num_img_heart = mysql_num_rows($result4);					
		
					$sql5 = "SELECT num_videos_likes FROM videos_like WHERE video_id='".$video_id."' AND num_videos_likes=1";
					$result5 = mysql_query($sql5) or die(mysql_error());
					$num_video_likes = mysql_num_rows($result5);
		 
					$sql6 = "SELECT num_videos_heart FROM videos_hearts WHERE video_id='".$video_id."' AND num_videos_heart=1";
					$result6 = mysql_query($sql6) or die(mysql_error());
					$num_video_heart = mysql_num_rows($result6);
					
					if(($image_id != "noimage") && ($image_url != "noimageurl"))
					{
						if(($video_id != "novideo") && ($video_url != "novideourl"))
						{
							$posts = array(array('ProfileName'=>$profilename,'ImageID'=>$image_id,'ImageUrl'=>$image_url,'Likes'=>$num_img_likes,'Hearts'=>$num_img_heart,"CurrTime"=>$ImageCurrTime),array('ProfileName'=>$profilename,'VideoID'=>$video_id,'VideoUrl'=>$video_url,'VideoThumb'=>$video_thumb_url,'Likes'=>$num_video_likes,'Hearts'=>$num_video_heart,"CurrTime"=>$VideoCurrTime));
						}
						else
						{
							$posts = array('ProfileName'=>$profilename,'ImageID'=>$image_id,'ImageUrl'=>$image_url,'Likes'=>$num_img_likes,'Hearts'=>$num_img_heart,"CurrTime"=>$ImageCurrTime);
						}
					}
					else
					{
						if(($video_id != "novideo") && ($video_url != "novideourl"))
						{
							$posts = array('ProfileName'=>$profilename,'VideoID'=>$video_id,'VideoUrl'=>$video_url,'VideoThumb'=>$video_thumb_url,'Likes'=>$num_video_likes,'Hearts'=>$num_video_heart,"CurrTime"=>$VideoCurrTime);
						}
						else
						{
							echo "no result";
						}
					}
					
					try
					{
						$abcd[] =array('result'=>$posts);
					}
					catch (Exception $e)
					{
						$status = false;
						$janit =array('Status'=>$status,'result'=>"No Record Exist Here");
					}
				}
				//$third = array_merge($first,$second);
				
				$janit = array('Status'=>$status,'result'=>$abcd);
			}
		}   /*End Third brace*/
	  
	}  /*End Second brace*/
	
	
	
	foreach($abcd as $val){
	   foreach($val as $value){
		$res_array[$value['CurrTime']][] = $value;
		}
	}
	
	
	krsort($res_array);
	
	$newArray = array();
		foreach($res_array as $key=>$val){
			foreach($val as $key=>$value){
				$newArray[] = $value;
			}
		}
	// echo "<pre>";
	// print_r($newArray);
	// echo "</pre>";
	$cnt = count($newArray);
	$endloop = (count($newArray))%10;
	//echo $loop = count($abcd)%10;
	$pages= 0;
	$page = $_REQUEST['page'];	
	if($page==1){
		$page =0;
	}else{
		$pages = ($page)*10;
		$page = 10*($page-1);
	}		

		$loops = ($cnt-$pages)+10;

	if($cnt>10){
		$loop=10+$page;
		if($endloop==$loops){
			$loop = $endloop+($page);
		}	
	}else{
		$loop = $cnt;
	}
	if($_REQUEST['page']==0){
		$loop = $cnt;
		$page =0;
	}
	if($loops>=1){
		for($i=$page;$i<$loop;$i++){
			$Finalarray[] = $newArray[$i];		
		}
		$janit =array('Status'=>$status,'result'=>$Finalarray);		
	}else{
		$janit =array('Status'=>false,'result'=>"No Record Exist Here");
	}
	
}   /*End first barace*/
else
{
	$status = false;
	$janit =array('Status'=>$status,'result'=>"No Record Exist Here");
}
 // echo "<pre>";
 // print_r($abcd);
 // echo "</pre>";
echo json_encode(($janit));
?>