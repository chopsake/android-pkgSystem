<?php

$key = 'KbnLHqqqZLw60uctCYh1';

if (isset($_POST['key']))
{
    if ($_POST['key'] == $key);
    else
        die('Key Not Authorized');
}

print_r($_POST);

?>

