<?php
$con = mysql_connect("localhost", "id5094472_bnv", "Fray1234");

if(!$con)
die('could not connect: ' .mysql_error());

mysql_select_db("id5094472_debarrio_db",$con);

$result = mysql_query("SELECT * FROM CONTACTO");
 
while($row=mysql_fetch_assoc($result)){
$output[]=$row;
}

print(json_encode($output));

mysql_close($con); ?>