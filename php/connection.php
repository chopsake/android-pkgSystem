<?php

// access url = countableset.com/myadmin

// Datebase varibales
$db_host = "localhost";
$db_user = "android";
$db_pass = "H6yFJ6rW96XvQFJG";

// Esstablish conection to MySQL database
$con = mysql_connect( $db_host, $db_user, $db_pass);

// Checking if there is a connection to the db if not print out error
if(!$con)
	die('Could not connect: ' . mysql_error() );

// Selecting the right db from te connection
mysql_select_db("countableset_android", $con);

?>
