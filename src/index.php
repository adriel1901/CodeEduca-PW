<?php
include 'conexao.php'; // Inclui nossa conexão
// --- LÓGICA DO CREATE (C) ---
// Verifica se o formulário foi enviado (método POST)
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nome = $_POST['nome'];
    $email = $_POST['email'];
// Prepara a query SQL para inserir os dados
    $sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";
    $stmt = $pdo->prepare($sql);
// Executa a query com os dados
    $stmt->execute([$nome, $email]);
// Redireciona para a própria página (para limpar o formulário)
    header("Location: index.php");
    exit;
}
// --- LÓGICA DO READ (R) ---
// Query SQL para selecionar todos os usuários
$sql = "SELECT * FROM usuarios";
$stmt = $pdo->query($sql); // Executa a query
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>CRUD com PHP</title>
</head>
<body>
    <h2>Adicionar Novo Usuário (Create)</h2>

    <form method="POST">
        <label>
            Nome:
            <input type="text" name="nome" required>
        </label>
        <br><br>

        <label>
            Email:
            <input type="email" name="email" required>
        </label>
        <br><br>

        <button type="submit">Salvar</button>
    </form>

    <hr>

    <h2>Usuários Cadastrados (Read)</h2>

    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Email</th>
            <th>Ações</th>
        </tr>

        <?php
        // Loop para listar os usuários do banco de dados
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            echo "<tr>";
            echo "<td>" . htmlspecialchars($row['id']) . "</td>";
            echo "<td>" . htmlspecialchars($row['nome']) . "</td>";
            echo "<td>" . htmlspecialchars($row['email']) . "</td>";
            echo "<td>";
            echo "<a href='editar.php?id=" . $row['id'] . "'>Editar</a> | ";
            echo "<a href='excluir.php?id=" . $row['id'] . "'>Excluir</a>";
            echo "</td>";
            echo "</tr>";
        }
        ?>
    </table>
</body>
</html>
