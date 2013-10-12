<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$condition = $_POST['cond'];

if($condition == 1)        //display the list of people
{
    $sql = "SELECT u.id, u.first_name, u.last_name\n"
    . "FROM users u\n"
    . "WHERE u.id NOT IN (SELECT friend_id\n"
    . "	FROM will_deliver\n"
    . "	WHERE user_id = " . $userID . ")"
    . "AND u.id <> " . $userID . " "  // don't want self to show up on list
    . "AND u.blacklist <> 5 "         // don't want blacklisted people
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
}
if($condition == 2)        //add the person they wanted
{
    $friend = $_POST['id'];
    $sql = "INSERT INTO will_deliver (`user_id`, `friend_id`) VALUES (" . $userID . ", " . $friend . ");";
//    $sql2 = "INSERT INTO friendships (`id`, `user_id`, `friend_id`) VALUES (NULL, " . $friend . ", " . $userID . ");";
    mysql_query($sql);
//    mysql_query($sql2);
}

mysql_close($con);

?>
