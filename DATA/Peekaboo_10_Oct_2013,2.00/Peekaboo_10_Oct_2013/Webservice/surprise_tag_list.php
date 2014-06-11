<?php

// http://192.168.1.196/PeekaBoo_Webservice/surprise_tag_list.php?uid=76
include('config.php');
$uid = $_REQUEST['uid'];

$select = "select * from surprise_tag where user_id='$uid'";
$res = mysql_query($select);
$num = mysql_num_rows($res);

$result= array();
if($num>0){		
	while($row = mysql_fetch_assoc($res)){
		if(!empty($row['tag'])){
			$status = true;	
			$tag[] = $row['tag'];
			$result = array("TagName"=>$tag);
		}else{
			$status = false;
			$result = "No record Exits";
		}
	}	
}
else{	
	$status = false;
	$result = "No record Exits";
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>