<?php
$servername="localhost";
$username="root";
$conn=  mysql_connect($servername,$username)or die(mysql_error());
mysql_select_db("webservice2",$conn);
?>