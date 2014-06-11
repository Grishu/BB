<?php

// http://192.168.1.196/PeekaBoo_Webservice/surprise_delete.php?uid=76&tag=janit
include('config.php');
$uid = $_REQUEST['uid'];
$tag = str_replace(",","','",$_REQUEST['tag']);

 $select = "select * from surprise_tag where user_id='$uid' and tag in('$tag')";
$res = mysql_query($select);
$num = mysql_num_rows($res);
$cnt = 0 ;
if($num>0){	
	$status = true;
	$insert = mysql_query("DELETE FROM surprise_tag where user_id='$uid' and tag in('$tag')");	
	$result = "Tag name Deleted Successfully";
}
else{	
	$status = false;	
	$result = "Tag name not extist";
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>