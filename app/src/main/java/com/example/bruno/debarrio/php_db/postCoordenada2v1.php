<?php
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db");
    
    $lat = $_POST["lat"];
	$lng = $_POST["lng"];
    $statement = mysqli_prepare($con, "INSERT INTO coordenadas (lat, lng) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ss", $lat, $lng);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>