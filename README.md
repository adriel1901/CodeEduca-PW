## ⚙️ Descrição Completa do Projeto e Tecnologias

### 💡 O que é o CodeEduca?

O **CodeEduca** é uma plataforma web dinâmica desenvolvida para ser um **Banco de Questões Online**. Seu objetivo principal é fornecer aos usuários as ferramentas necessárias para **criar, gerenciar, organizar e praticar** com grupos de questões personalizados sobre diversos assuntos. O sistema oferece:

* **Gerenciamento (CRUD):** Permite adicionar, editar, excluir e atualizar questões e tópicos.
* **Controle de Acesso:** Sistema de login, cadastro e edição de perfil.
* **Modo de Estudo:** Oferece quizzes públicos e privados para prática contínua e autoavaliação do conhecimento.

---

### 🔗 Contribuição com a ODS 4: Educação de Qualidade

O CodeEduca contribui de forma **direta e significativa** para o **Objetivo de Desenvolvimento Sustentável 4 (ODS 4)** da ONU, que visa "Assegurar a educação inclusiva e equitativa e de qualidade, e promover oportunidades de aprendizagem ao longo da vida para todos".

* **Apoio à Meta 4.4 (Habilidades Relevantes):** Ao focar em temas de programação e diversos, o projeto fornece um meio acessível para que jovens e adultos possam **adquirir e testar habilidades técnicas e vocacionais** essenciais no mercado de trabalho atual.
* **Promoção da Aprendizagem ao Longo da Vida (Meta 4.3):** Como uma ferramenta de estudo online e flexível, o CodeEduca permite que os usuários personalizem seu aprendizado e pratiquem continuamente, promovendo a educação a qualquer tempo e fase da vida.

---

### 🖥️ Ferramentas e Tecnologias Utilizadas

O projeto é construído sobre uma arquitetura robusta de desenvolvimento web em Java, utilizando o padrão **MVC (Model-View-Controller)** para separar a lógica do negócio da interface do usuário.

| Tecnologia | Função na Aplicação | Uso no CodeEduca |
| :--- | :--- | :--- |
| **Java** | Linguagem de Programação | Linguagem fundamental utilizada para implementar toda a **lógica de *backend***, desde validações de login até as operações **CRUD** sobre o banco de questões. |
| **NetBeans IDE** | Ambiente de Desenvolvimento | IDE utilizada para escrever, compilar, testar e realizar o *deploy* (publicação) do código Java Web. |
| **Apache Tomcat** | Servidor de Aplicações | Essencial para a **execução** da aplicação. Ele recebe as requisições HTTP e atua como o **contêiner** que gerencia e executa os Servlets e JSPs. |
| **Servlets** | **Controller** (Controlador) | Recebem as requisições do usuário, processam a lógica de negócio (ex: busca no banco de dados via JDBC) e encaminham os dados processados para as JSPs. |
| **JSP (JavaServer Pages)** | **View** (Visualizador) | Responsáveis pela **interface do usuário**. Elas misturam HTML com código Java (que gera conteúdo dinâmico) para montar a página final que é enviada ao navegador. |
| **JDBC** | Conectividade com DB | API utilizada para estabelecer a **conexão** e realizar as operações de leitura e escrita com o Banco de Dados. |
| **Banco de Dados** | Persistência de Dados | SGBD Relacional (a ser definido, ex: MySQL/PostgreSQL) para armazenar usuários, tópicos e todas as questões do sistema. |
