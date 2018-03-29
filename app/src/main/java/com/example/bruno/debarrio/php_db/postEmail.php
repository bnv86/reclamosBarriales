<?php
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db");
    
    $email = $_POST["email"];
    $detalle = $_POST["detalle"];
    $statement = mysqli_prepare($con, "INSERT INTO contacto_email (email, detalle) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ss", $email, $detalle);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>