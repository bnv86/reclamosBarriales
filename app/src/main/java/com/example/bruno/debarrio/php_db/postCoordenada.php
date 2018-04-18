<?php
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db");
    
    $coordenadas = $_POST["coordenadas"];
    $statement = mysqli_prepare($con, "INSERT INTO coordenadas (coordenadas) VALUES (?)");
    mysqli_stmt_bind_param($statement, "s", $coordenadas);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>