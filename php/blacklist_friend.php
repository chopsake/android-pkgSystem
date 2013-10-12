<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$blacklistId = $_POST['removeid'];  //id of user to be blacklisted

$sql = "SELECT blacklist FROM users WHERE id=" . $blacklistId . ";";
$result = mysql_query($sql);
$row = mysql_fetch_array($result);

$blacklist = $row['blacklist'] + 1;

$sql = "UPDATE users set blacklist=" . $blacklist . " WHERE id=" . $blacklistId . ";";
mysql_query($sql);

if($blacklist == 5)
{
    $friends = "DELETE FROM friendships WHERE user_id = " . $blacklistId . " OR friend_id = " . $blacklistId . ";";
    $willing = "DELETE FROM will_deliver WHERE user_id = " . $blacklistId . " OR friend_id = " . $blacklistId . ";";

    mysql_query($friends);
    mysql_query($willing);
}

?>
