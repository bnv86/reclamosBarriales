<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
    $fecha = $_POST['fecha'];
    $usuario = $_POST['usuario'];
    $foto = $_POST['foto'];
	$latitud = $_POST['latitud'];
	$longitud = $_POST['longitud'];
    $motivo = $_POST['motivo'];
    $comentario = $_POST['comentario'];
     
    require_once('dbConexion.php');
     
    $sql ="SELECT id FROM evento ORDER BY id ASC";
    $res = mysqli_query($con,$sql);
    $id = 0;
     
    while($row = mysqli_fetch_array($res)){
        $id = $row['id'];
 }
 
 $path = "fotos/$id.png";
 $actualpath = "https://momentary-electrode.000webhostapp.com/$path";
 $sql = "INSERT INTO evento (fecha, usuario, foto, latitud, longitud, motivo, comentario) VALUES ('$fecha', '$usuario', '$actualpath', '$latitud', '$longitud', '$motivo', '$comentario')";
 
 if(mysqli_query($con,$sql)){
    file_put_contents($path,base64_decode($foto));
    echo "Evento subido con exito!";
 }
 
 mysqli_close($con);
 
 }else{
    echo "Error al subir evento";
 }