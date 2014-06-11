<?php

// http://192.168.1.196/PeekaBoo_Webservice/surprise_add.php?uid=76&tag=janit,user
include('config.php');
$uid = $_REQUEST['uid'];
$tag = explode(",",$_REQUEST['tag']);
$tt = "'".str_replace(",","','",$_REQUEST['tag'])."'";
$cnt = count($tag);

$select = "select * from surprise_tag where user_id='$uid' and tag in($tt)";
$res = mysql_query($select);
$num = mysql_num_rows($res);
$i=0;

if($num<=0){	
	foreach($tag as $tags){		
		$status = true;
		$select_tag = "select * from surprise_tag where user_id='$uid' and tag ='".$tags."'";
		$res_tag = mysql_query($select_tag);
		$num_tag = mysql_num_rows($res_tag);
		
		if($num_tag>0){
			$status = false;
			$result = "Tag name already extist";
		}
		else{	
			$insert = mysql_query("INSERT INTO surprise_tag(`user_id`,`tag`) VALUES('".$uid."','".$tags."')");
			$inserted = mysql_insert_id();
			if($inserted){
				$result = "Tag name added Successfully";
			}else{
				$result = "Tag name not added, Please try later";
			}			
		}
	}
}
else{	
	$status = false;	
	$result = "Tag name already extist";
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>