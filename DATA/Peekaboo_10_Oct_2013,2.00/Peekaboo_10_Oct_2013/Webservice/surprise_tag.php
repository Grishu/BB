<?php

// http://192.168.1.196/PeekaBoo_Webservice/surprise_tag.php?uid=76
include('config.php');
$uid = $_REQUEST['uid'];

$select = "select * from surprise_tag where user_id='$uid'";
$res = mysql_query($select);
$num = mysql_num_rows($res);
$cnt = 0 ;
$tags = '';
if($num>0){	
	$status = true;	
	while($row = mysql_fetch_assoc($res)){
		$select_tag = "select tag,user_id,image_name,image_url FROM hashtag where tag='".$row['tag']."'";
		$tags[] = $row['tag'];
		$res_tag = mysql_query($select_tag);		
		while($row_tag = mysql_fetch_assoc($res_tag)){
			$select_user = mysql_query("select user_name from users where user_id=".$row_tag['user_id']);
			$users = mysql_fetch_assoc($select_user);
			
			if(!empty($row_tag['image_id'])){
				$select_image_likes = mysql_query("select count(num_of_likes) as cnt from likes where image_id='".$row_tag['image_id']."'");
				$likes = mysql_fetch_assoc($select_image_likes);
				
				$select_image_hearts = mysql_query("select count(num_of_hearts) as cnt from  hearts where image_id='".$row_tag['image_id']."'");
				$hearts = mysql_fetch_assoc($select_image_hearts);
				
				$profile_pic = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row_tag['image_url'];
				$result[] = array("UserID"=>$row_tag['user_id'],"Profile_Name"=>$users['user_name'],"ImageID"=>$row_tag['image_id'],"MediaUrl"=>$profile_pic,"Likes"=>$likes['cnt'],"Hearts"=>$hearts['cnt'],"Tag"=>$row_tag['tag']);
				$cnt++;
			}
		}
	}		
	if($cnt!=20){
		$start = 0;
		$ntag = "";
		foreach($tags as $tg){
			$ntag .= $tg;
			$start++;
			if($start!=count($tags)){
				$ntag .="','";
			}					
		}
		$select2 = "select * from hashtag where user_id != '$uid' AND tag!='' AND tag not in('".$ntag."')   order by RAND() DESC limit 0,20 ";
		$res2 = mysql_query($select2);
		while($row2 = mysql_fetch_assoc($res2)){
			$select_user = mysql_query("select user_name from users where user_id=".$row2['user_id']);
			$users = mysql_fetch_assoc($select_user);
			
			$select_image_likes = mysql_query("select count(num_of_likes) as cnt from likes where image_id='".$row2['image_id']."'");
			$likes = mysql_fetch_assoc($select_image_likes);
			
			$select_image_hearts = mysql_query("select count(num_of_hearts) as cnt from  hearts where image_id='".$row2['image_id']."'");
			$hearts = mysql_fetch_assoc($select_image_hearts);
		
			$profile_pic2 = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
			$result[] = array("UserID"=>$row2['user_id'],"Profile_Name"=>$users['user_name'],"ImageID"=>$row2['image_id'],"MediaUrl"=>$profile_pic2,"Likes"=>$likes['cnt'],"Hearts"=>$hearts['cnt'],"Tag"=>$row2['tag']);
			$cnt++;
			if($cnt==20){
				break;
			}
		}
	}
	
}
else{	
	$status = true;
	$select2 = "select * from hashtag where user_id != '$uid' and tag!='' order by RAND() DESC limit 0,20 ";
	$res2 = mysql_query($select2);
	while($row2 = mysql_fetch_assoc($res2)){
		$select_user = mysql_query("select user_name from users where user_id=".$row2['user_id']);
		$users = mysql_fetch_assoc($select_user);
		$profile_pic2 = "http://".$_SERVER['HTTP_HOST']."/PeekaBoo_Webservice/".$row2['image_url'];
		
		$select_image_likes = mysql_query("select count(num_of_likes) as cnt from likes where image_id='".$row2['image_id']."'");
		$likes = mysql_fetch_assoc($select_image_likes);
		
		$select_image_hearts = mysql_query("select count(num_of_hearts) as cnt from  hearts where image_id='".$row2['image_id']."'");
		$hearts = mysql_fetch_assoc($select_image_hearts);
		
		$result[] = array("UserID"=>$row2['user_id'],"Profile_Name"=>$users['user_name'],"ImageID"=>$row2['image_id'],"MediaUrl"=>$profile_pic2,"Likes"=>$likes['cnt'],"Hearts"=>$hearts['cnt'],"Tag"=>$row2['tag']);
		$cnt++;
		if($cnt==20){
			break;
		}
	}
}
echo json_encode(array("Status"=>$status,"result"=>$result));
?>