<?php
//http://192.168.1.196/PeekaBoo_Webservice/diceroll.php?uid=76&latitude=73.181098&longitude=22.307309														
include('config.php');
$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$uid = $_GET['uid'];
$date = date('Y-m-d H:i:s');
//echo $date;
//exit();
	$select_user = mysql_query("SELECT `user_id` FROM `diceroll` WHERE `user_id`=$uid");
	$num_rows = mysql_num_rows($select_user);
	
	if($num_rows>0){
		$update = mysql_query("UPDATE `diceroll` SET `latitude`=$latitude,`longitude`=$longitude,`curr_timestemp`='".$date."' WHERE `user_id`=$uid",$conn) or die(mysql_error());
	}else{
		$insert = mysql_query("INSERT INTO `diceroll` (`longitude`, `latitude`, `user_id`, `curr_timestemp`) VALUES ($longitude,$latitude,$uid,'".$date."')",$conn) or die(mysql_error());
	}

	$select = "	SELECT   user_id,
				(6371 * ACOS(
							SIN(RADIANS( $latitude )) * SIN(RADIANS(`latitude`)) +
							COS(RADIANS( $latitude )) * COS(RADIANS(`latitude`)) * 
							COS(RADIANS(`longitude`) - RADIANS( $longitude ))
						)
				) AS `distance`
			FROM  `diceroll` WHERE
			user_id not in($uid) AND `curr_timestemp` >= date_sub('".$date."', interval 5 minute)
			GROUP BY user_id
			ORDER BY `curr_timestemp` DESC
			LIMIT 0,12"; 

	$result=mysql_query($select,$conn) or die(mysql_error());
	
	
	$posts = array();
	$records = array();
	$total_records = mysql_num_rows($result);
	
	if($total_records < 1)
	{
		$status= false;
		$posts = array('Status'=>$status,'result'=>"No Data Found");
	}
	else
	{
		$status= true;

		while($row = mysql_fetch_assoc($result)){
			$user_id = $row['user_id'];
			$select2 = mysql_query("SELECT user_name,image_url FROM users,images WHERE users.user_id=$user_id and users.user_id=images.user_id AND images.profileflag='1'",$conn) or die(mysql_error());
			
			$new_num = mysql_num_rows($select2);
			if($new_num < 1)
			{
				$no_image = mysql_query("Select user_name from users where user_id = $user_id", $conn);
				while($no_image_row = mysql_fetch_assoc($no_image))
				{
					$profilename = $no_image_row['user_name'];
					$distance = round(($row['distance'] * 1.609344),2);
					$image_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
					$records[] = array('ID'=>$user_id,'ProfileName'=>$profilename,'Distance'=>$distance,'ImageUrl'=>$image_url);
				}
			}
			else
			{
				while($row2 = mysql_fetch_assoc($select2)){
					$profilename = $row2['user_name'];
					$distance = round(($row['distance'] * 1.609344),2);
					$image_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
					$records[] = array('ID'=>$user_id,'ProfileName'=>$profilename,'Distance'=>$distance,'ImageUrl'=>$image_url);
				}
			}			
		}
		$posts = array('Status'=>$status,'result'=>$records,'Users'=>$total_records);
	}
	
	echo json_encode(($posts));
?>