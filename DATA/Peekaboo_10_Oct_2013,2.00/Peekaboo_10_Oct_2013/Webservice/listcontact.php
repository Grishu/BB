<?php
//http://192.168.1.196/PeekaBoo_Webservice/listcontact.php?uid=76&page=1
include('config.php');
$uid = $_GET['uid'];
$sql = "SELECT * FROM contacts WHERE user_id=$uid AND contact_flag = '1'";
$result = mysql_query($sql) or die(mysql_error());
$no_of_records = mysql_num_rows($result);

$per_page = 10;
$total_pages = ceil($no_of_records / $per_page);
$page_count =$_GET['page'];
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



$posts = array();
$sql = "SELECT * FROM contacts WHERE user_id=$uid AND contact_flag = '1' LIMIT $start,$per_page";
if($page_count==0)
$sql = "SELECT * FROM contacts WHERE user_id=$uid AND contact_flag = '1'";
$result = mysql_query($sql) or die(mysql_error());
  if($no_of_records > 0){
while($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
	$Status = true;
	$contact_id = $row['contact_id'];
	
	//echo $follower_id . "<br>";
	$sql2 = "SELECT a.user_name, b.image_url FROM users a, images b WHERE a.user_id=$contact_id AND b.user_id=$contact_id and b.profileflag='1'";
	$result2 = mysql_query($sql2) or die(mysql_error());
	$num = mysql_num_rows($result2);
	if($num < 1)
		{
			$sqlj = "SELECT user_name FROM users WHERE user_id=$contact_id";
			$resultj = mysql_query($sqlj);
			while($rowj = mysql_fetch_array($resultj, MYSQL_ASSOC))
			{
				
				$contact_name = $rowj['user_name'];
				$contact_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/uploads/default/default.png";
				$posts[] = array('ID'=>$contact_id,'ProfileName'=>$contact_name,'ImageUrl'=>$contact_image);
		        $janit = array('Status'=>$Status,'result'=>$posts);
			}
		}
		else
		{
	while($row2 = mysql_fetch_array($result2, MYSQL_ASSOC))
	{
		$contact_name = $row2['user_name'];
		$contact_image = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
		
		//echo $follower_name . "<br>" . $follower_image;
		$posts[] = array('ID'=>$contact_id,'ProfileName'=>$contact_name,'ImageUrl'=>$contact_image);
		$janit = array('Status'=>$Status,'result'=>$posts);
	}
		}
}
  }
  else
  {
	  $Status = false;
	  $janit = array('Status'=>$Status,'result'=>"No Record Exist Here");
  }
  }else{
	$Status = false;
	  $janit = array('Status'=>$Status,'result'=>"No Record Exist Here");
  }
 
   echo json_encode(($janit));
?>