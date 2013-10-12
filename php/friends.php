<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$sql = "SELECT u.id, u.first_name, u.last_name\n"
    . "FROM users u, (SELECT friend_id\n"
    . "	FROM friendships\n"
    . "	WHERE user_id = ". $userID . ") f\n"
    . "WHERE u.id = f.friend_id\n"
    . "ORDER BY u.first_name ASC, u.last_name ASC;";

$result = mysql_query($sql);
$rows = mysql_num_rows($result);

$data = array();

if($rows > 0)
{
    while($row = mysql_fetch_array($result))
    {
        $temp = $row['first_name'] . " " . $row['last_name'];
        $data['names'][] = $temp;
        $data['names'][] = $row['id'];
    }
    echo json_encode($data);
}

mysql_close($con);

?>
