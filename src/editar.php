<?php
include 'conexao.php';

$id = $_GET['id'];

// Busca o usuário específico no banco
$sql = "SELECT * FROM usuarios WHERE id = ?";
$stmt = $pdo->prepare($sql);
$stmt->execute([$id]);
$usuario = $stmt->fetch(PDO::FETCH_ASSOC); // Pega os dados

// Se não encontrar o usuário, volta para o index
if (!$usuario) {
    header("Location: index.php");
    exit;
}
?>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Editar Usuário</title>
</head>
<body>
    <h2>Editar Usuário</h2>

    <form action="atualizar.php" method="POST">
        <input type="hidden" name="id" value="<?php echo $usuario['id']; ?>">

        <label>
            Nome:
            <input type="text" name="nome" value="<?php echo htmlspecialchars($usuario['nome']); ?>" required>
        </label>
        <br><br>

        <label>
            Email:
            <input type="email" name="email" value="<?php echo htmlspecialchars($usuario['email']); ?>" required>
        </label>
        <br><br>

        <button type="submit">Atualizar</button>
    </form>
</body>
</html>
