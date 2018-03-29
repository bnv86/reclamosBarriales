<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
    $foto = $_POST['foto'];
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
 $sql = "INSERT INTO evento (foto,motivo,comentario) VALUES ('$actualpath','$motivo', '$comentario')";
 
 if(mysqli_query($con,$sql)){
    file_put_contents($path,base64_decode($foto));
    echo "Evento subido con exito!";
 }
 
 mysqli_close($con);
 
 }else{
    echo "Error al subir evento";
 }