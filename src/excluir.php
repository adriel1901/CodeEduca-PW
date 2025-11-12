<?php
include 'conexao.php';
// Pega o ID da URL (via GET)
$id = $_GET['id'];
// Prepara e executa a query de exclusão
$sql = "DELETE FROM usuarios WHERE id = ?";
$stmt = $pdo->prepare($sql);
$stmt->execute([$id]);
// Redireciona de volta para a lista (index.php)
header("Location: index.php");
exit;
?>