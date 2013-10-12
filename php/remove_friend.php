<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$removeId = $_POST['removeid'];  //id of user to remove from relationship table


$sql = "DELETE FROM friendships WHERE user_id =\"" . $userID . "\" AND friend_id =\"" . $removeId . "\";";

$result = mysql_query($sql);

$data['response'][] = 'Done';

echo json_encode($data);

mysql_close($con);

?>
