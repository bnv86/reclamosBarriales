<?php
    //$con = mysqli_connect("localhost", "root", "", "deBarrio_db"); //LOCAL
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db"); //REMOTO
    
    $username = $_POST["username"];
    $imagen = $_POST["imagen"];
	//$evento_id = $_POST["evento_id"];
    $statement = mysqli_prepare($con, "INSERT INTO foto (username, imagen) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ssis", $username, $imagen);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>