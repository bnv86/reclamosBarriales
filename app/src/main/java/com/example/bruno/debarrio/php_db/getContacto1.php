<?php
    //$con = mysqli_connect("localhost", "root", "", "deBarrio_db"); //LOCAL
	$con = mysqli_connect("localhost", "id5094472_bnv", "Fray1234", "id5094472_debarrio_db"); //REMOTO
    
    $telefono = $_GET["telefono"];
    $email = $_GET["email"];
    $direccion = $_GET["direccion"];
    $detalle = $_GET["detalle"];
    $statement = mysqli_prepare($con, "SELECT * FROM contacto");
    mysqli_stmt_bind_param($statement, "ssis", $telefono, $email, $direccion, $detalle);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>