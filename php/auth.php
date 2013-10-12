<?php

$email =    $_POST['email'];
$pass =     $_POST['password'];

$query =    "SELECT id FROM users WHERE email=\"" . $email . "\" AND password=MD5(\"" . $pass . "\");";

$result = mysql_query($query);
$rows = mysql_num_rows($result);

if($rows == 1)
{
    $row = mysql_fetch_array($result);
    $userID = $row['id'];
}
else
    die('No User Found');

?>
