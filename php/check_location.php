<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$sql = "SELECT latitude, longitude FROM users WHERE id=" . $userID . ";";

$result = mysql_query($sql);
$num = mysql_num_rows($result);

if($num == 1)
{
    $row = mysql_fetch_array($result);
    $data = array();

    if($row['latitude'] == null || $row['longitude'] == null)
        $data['response'][] = 0;
    else
        $data['response'][] = 1;

    echo json_encode($data);
}


mysql_close($con);

?>
