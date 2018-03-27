<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $imagen= $_POST['foto'];
 $nombre = $_POST['nombre'];
 
 require_once('dbConexion.php');
 
 $sql ="SELECT id FROM imagen ORDER BY id ASC";
 
 $res = mysqli_query($con,$sql);
 
 $id = 0;
 
 while($row = mysqli_fetch_array($res)){
    $id = $row['id'];
 }
 
 $path = "fotos/$id.png";
 $actualpath = "https://momentary-electrode.000webhostapp.com/$path";
 
 $sql = "INSERT INTO imagen (foto,nombre) VALUES ('$actualpath','$nombre')";
 
 if(mysqli_query($con,$sql)){
     file_put_contents($path,base64_decode($imagen));
     echo "Foto subida con éxito!";
 }
 
 mysqli_close($con);
 }else{
     echo "Error al subir foto";
 }