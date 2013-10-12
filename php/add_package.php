<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

include('auth.php');       //authenticates the user and returns their id (userID)

$condition = $_POST['cond'];
$data = array();
$dest = $_POST['id'];

if($condition == 1)        //display the list of all users except self
{
    $sql = "SELECT distinct u.id, u.first_name, u.last_name\n"
    . "FROM users u\n"
    . "WHERE u.id <> " . $userID . " "  // don't want self to show up on list
    . "ORDER BY u.first_name ASC, u.last_name ASC;";


    $result = mysql_query($sql);
    $rows = mysql_num_rows($result);

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
else if($condition == 2)    //sending package directly
{
    $prefix = 'P'; // a universal prefix prefix
    $pkg_id = $prefix;
    $pkg_id .= chr(rand(65,90));
    $pkg_id .= time();
    $pkg_id .= uniqid($prefix);

    // See If you are willing to deliver to destination user.
    $sql = "Select friend_id From will_deliver\n"
    . "Where user_id = " . $userID . " and friend_id = " . $dest . "\n;";
    $result = mysql_query($sql);
    $rows = mysql_num_rows($result);

    // You are willing to deliver. So add to database.
    if ($rows > 0)
    {
        $pkg_query = "INSERT INTO packages (`pkg_id`, `src_id`,`dest_id`) VALUES (\"" . $pkg_id . "\", " . $userID . ", " . $dest . ");";
        $r_pkg_query = "INSERT INTO r_packages(`pkg_id`,`current_id`,`next_id`,`send_date`,`has`) VALUES(\"" . $pkg_id . "\", " . $userID . ", " . $dest . ", NOW(),1);";

        mysql_query($pkg_query);
        mysql_query($r_pkg_query);
        
        $data['names'][] = '0';
        echo json_encode($data);
    }
    // You are not willing to deliver so send to Post Office.
    else
    {
        $data['names'][] = '1';
        echo json_encode($data);
    }
}
else if($condition == 3)            //sending packge with routeing
{
    include("searchalg.php");       // return $solution with the path array

    // No path or only a path between the source and the destination
    if(count($solution) <= 2)
    {
        // No one is willing to deliver package, so send to Post Office.
        $data = array();
        $data['names'][] = '1';
        echo json_encode($data);
    }
    else
    {
        $prefix = 'P';              // a universal prefix prefix
        $pkg_id = $prefix;
        $pkg_id .= chr(rand(65,90));
        $pkg_id .= time();
        $pkg_id .= uniqid($prefix);

        $pkg_query = "INSERT INTO packages (`pkg_id`, `src_id`,`dest_id`) VALUES (\"" . $pkg_id . "\", " . $userID . ", " . $dest . ");";
        mysql_query($pkg_query);

        for($a = 0; $a < count($solution) - 1; $a++)
        {
            $u1 = $solution[$a];
            $u2 = $solution[$a+1];
            $r_pkg = "INSERT INTO r_packages(`pkg_id`,`current_id`,`next_id`,`send_date`) VALUES(\"" . $pkg_id . "\", " . $u1 . ", " . $u2 . ", NOW());";
            mysql_query($r_pkg);
        }
        // set the curent user flag to show package to be delivered.
        $update = "UPDATE r_packages SET has=1 WHERE current_id = $userID and '" .  $pkg_id . "' = pkg_id ;"; 
        mysql_query($update);

        $data = array();
        $data['names'][] = '0';
        echo json_encode($data);

    }
}

mysql_close($con);

?>
