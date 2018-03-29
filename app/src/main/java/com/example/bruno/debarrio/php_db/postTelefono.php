<?php
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db");
    
    $telefono = $_POST["telefono"];
    $detalle = $_POST["detalle"];
    $statement = mysqli_prepare($con, "INSERT INTO contacto_telefono (telefono, detalle) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "is", $telefono, $detalle);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>