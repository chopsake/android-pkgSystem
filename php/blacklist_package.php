<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database
include('auth.php');       //authenticates the user and returns their id (userID)

$removeid = $_POST['removeid'];  // pkg_id to be removed

// from Pending menu, a Blacklist will remove that pkg from system
$sql = "DELETE FROM r_packages WHERE pkg_id =\"" . $removeid . "\";";
$result = mysql_query($sql);

$sql = "DELETE FROM packages WHERE pkg_id =\"" . $removeid . "\";";
$result = mysql_query($sql);

$data['response'][] = 'Done';

echo json_encode($data);

mysql_close($con);

?>
