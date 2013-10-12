<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database
include('auth.php');       //authenticates the user and returns their id (userID)

$removeId = $_POST['removeid'];  //delivered to id

$sql = "Select next_id From r_packages where current_id = $userID and pkg_id ='$removeId';";
$result = mysql_query($sql);
$rows = mysql_num_rows($result);
if ($rows > 0)
{
    $row = mysql_fetch_array($result);
    $next = $row['next_id'];
    $sql = "Update r_packages Set has=1 Where current_id = $next and pkg_id = '$removeId';";
    $result = mysql_query($sql);
}



$sql = "DELETE FROM r_packages WHERE current_id =\"" . $userID . "\" AND pkg_id =\"" . $removeId . "\";";
$result = mysql_query($sql);


$sql = "SELECT id FROM r_packages WHERE pkg_id =\"" . $removeId . "\";";
$result = mysql_query($sql);

$rows = mysql_num_rows($result);
if ($rows == 0)
{
    $sql = "DELETE FROM packages WHERE pkg_id =\"" . $removeId . "\";";
    $result = mysql_query($sql);
}


$data['response'][] = 'Done';

echo json_encode($data);

mysql_close($con);

?>
