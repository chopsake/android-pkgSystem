<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$action = $_POST["action"];

if($action == 1)           //population of the fields
{
    $sql = "SELECT * FROM users WHERE id=" . $userID . ";";

    $result = mysql_query($sql);
    $rows = mysql_num_rows($result);

    $data = array();

    $row = mysql_fetch_array($result);
    $data['info'][] = $row['first_name'];
    $data['info'][] = $row['last_name'];
    $data['info'][] = $row['email'];
    $data['info'][] = $row['status'];

    echo json_encode($data);
}
elseif($action == 2)       //action = 2, update information
{
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];

    $sql = "UPDATE users SET first_name=\"" . $fname . "\", last_name=\"" . $lname . "\" WHERE id=" . $userID . ";";


    mysql_query($sql);
}
elseif($action == 3)
{
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];

    $sql = "UPDATE users SET latitude=" . $lat . ", longitude=" . $lng . " WHERE id=" . $userID . ";";

    mysql_query($sql);
}

mysql_close($con);

?>
