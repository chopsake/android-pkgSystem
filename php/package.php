<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database
include('auth.php');       //authenticates the user and returns their id (userID)

$condition = $_POST['cond'];
if($condition == 1)
{
    $sql = "SELECT u.id, u.first_name, u.last_name, r.pkg_id\n"
        . "FROM users u, r_packages r\n"
        . "WHERE r.current_id = ". $userID . " and r.next_id = u.id and r.has = 1\n"
        . "ORDER BY u.first_name ASC, u.last_name ASC, r.pkg_id ASC;";

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
            $data['names'][] = $row['pkg_id'];
        }
        echo json_encode($data);
    }
}

if($condition == 2)
{
    $sql = "SELECT u.id, u.first_name, u.last_name, p.pkg_id\n"
        . "FROM users u, packages p\n"
        . "WHERE p.src_id = ". $userID . " and p.dest_id = u.id\n"
        . "ORDER BY u.first_name ASC, u.last_name ASC, pkg_id ASC;";

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
            $data['names'][] = $row['pkg_id'];
            $temp = $row['pkg_id'];
            $sql2 = "SELECT u.id, u.first_name, u.last_name\n"
                 . "FROM users u, r_packages p\n"
                 . "WHERE u.id = p.current_id and p.pkg_id = '$temp' and p.has = 1;";
            $result2 = mysql_query($sql2);
            $row2 = mysql_fetch_array($result2);
            $temp = $row2['first_name'] . " " . $row2['last_name'];
            $data['names'][] = $temp;
            $data['names'][] = $row2['id'];
        }
        echo json_encode($data);
    }
}

mysql_close($con);

?>
