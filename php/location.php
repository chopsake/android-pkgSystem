<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database
include('auth.php');       //authenticates the user and returns their id (userID)

$user = $_POST['user'];

// return last known location for queried user
$sql = "SELECT latitude, longitude FROM users WHERE id=" . $user . ";";

$result = mysql_query($sql);
$rows = mysql_num_rows($result);

$data = array();

$row = mysql_fetch_array($result);
$data['location'][] = $row['latitude'];
$data['location'][] = $row['longitude'];

echo json_encode($data);

mysql_close($con);

?>
