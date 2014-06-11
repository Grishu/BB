<?php
//http://192.168.1.196/PeekaBoo_Webservice/looksarround.php?uid=76&latitude=73.181098&longitude=22.307309&page=1
include('config.php');
$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$uid = $_GET['uid'];

	$select_user = mysql_query("SELECT `user_id` FROM `location` WHERE `user_id`=$uid");
	$num_rows = mysql_num_rows($select_user);
	
	if($num_rows>0){
		$update = mysql_query("UPDATE `location` SET `latitude`=$latitude,`longitude`=$longitude WHERE `user_id`=$uid",$conn) or die(mysql_error());
	}else{
		$insert = mysql_query("INSERT INTO `location` (`longitude`, `latitude`, `user_id`) VALUES ($longitude,$latitude,$uid)",$conn) or die(mysql_error());
	}

	$select = "	SELECT   user_id,
				(6371 * ACOS(
							SIN(RADIANS( $latitude )) * SIN(RADIANS(`latitude`)) +
							COS(RADIANS( $latitude )) * COS(RADIANS(`latitude`)) * 
							COS(RADIANS(`longitude`) - RADIANS( $longitude ))
						)
				) AS `distance` 
			FROM  `location` WHERE
			user_id not in($uid)
			GROUP BY user_id
			ORDER BY `distance` ASC
			LIMIT 0,50"; // Query find distance in KM

	$result=mysql_query($select,$conn) or die(mysql_error());
	
	$posts = array();
	$records = array();
	$total_records = mysql_num_rows($result);
	
	$page_count = $_GET['page'];
	
	$per_page = 10;
	$total_pages = ceil($total_records / $per_page);
	if($total_pages>=$page_count){
		if (isset($_GET['page'])) {
			$show_page = $_GET['page'];             //it will telles the current page
			
			if ($show_page > 0 && $show_page <= $total_pages) {
				$start = ($show_page - 1) * $per_page;
				$end = $start + $per_page;
				// echo $start . "<br>";
				// echo $end . "<br>";
			} else {
				// error - show first set of results
				$start = 0;              
				$end = $per_page;
			}
		} else {
			// if page isn't set, show first set of results
			$start = 0;
			$end = $per_page;
		}
		// display pagination
		$page = intval($_GET['page']);
		
		$tpages=$total_pages;
		if ($page < 0)
			$page = 1;
			
		$select = "	SELECT   user_id,
					(6371 * ACOS(
								SIN(RADIANS( $latitude )) * SIN(RADIANS(`latitude`)) +
								COS(RADIANS( $latitude )) * COS(RADIANS(`latitude`)) * 
								COS(RADIANS(`longitude`) - RADIANS( $longitude ))
							)
					) AS `distance` 
				FROM  `location` WHERE
				user_id not in($uid)
				GROUP BY user_id
				ORDER BY `distance` ASC"; // Query find distance in KM
		if($_GET['page']!=0)
			$select .= "	LIMIT $start,$per_page"; // Query find distance in KM

		$result=mysql_query($select,$conn) or die(mysql_error());	
		
		
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
				$select2 = "SELECT user_name,user_description FROM users WHERE users.user_id=$user_id";
				$select2 = mysql_query($select2);
				while($row2 = mysql_fetch_assoc($select2)){
					$profilename = $row2['user_name'];
					$distance = round(($row['distance'] * 1.609344),2);
					$user_description = $row2['user_description'];
				}					
				
				$select_image = "select image_url from images where user_id=$user_id And profileflag='1'";
				$select_image = mysql_query($select_image);
				
				$row3 = mysql_fetch_assoc($select_image);			
				if(mysql_num_rows($select_image)>0){				
					$image_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row3['image_url'];
				}else{
					$image_url = $image_url = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				}
				$records[] = array('ID'=>$user_id,'ProfileName'=>$profilename,'Distance'=>$distance,'ImageUrl'=>$image_url,"UserDescription"=>$user_description);						
			}

			$posts = array('Status'=>$status,'result'=>$records);
		}
	}else{
		$status= false;
		$posts = array('Status'=>$status,'result'=>"No Records Found");
	}
	
	echo json_encode(($posts));
?>