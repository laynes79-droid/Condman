# Guia de Testes de Aceitação do Usuário (UAT) - CondoManager

Olá! Este documento foi preparado pelo seu **Agente Assistente** para guiá-lo no processo de compilação e teste do aplicativo CondoManager. Siga os passos abaixo para validar as funcionalidades que implementamos juntos.

## Pré-requisitos

1.  **Git**: Para baixar o código-fonte.
2.  **Android Studio**: Para compilar e rodar o app.
3.  **Python**: Para rodar o backend.

---

## Passo 1: Obtendo o Código-Fonte

1.  Abra um terminal (Git Bash, cmd, PowerShell).
2.  Navegue até a pasta onde deseja salvar o projeto.
3.  Execute o comando para clonar o repositório:
    ```bash
    git clone <URL_DO_REPOSITORIO> CondoManager
    ```
    *(Substitua `<URL_DO_REPOSITORIO>` pela URL correta).*

---

## Passo 2: Configurando e Rodando o Backend

1.  No seu terminal, navegue para a pasta do backend: `cd CondoManager/backend`
2.  Crie um ambiente virtual: `python -m venv venv`
3.  Ative o ambiente virtual: `venv\Scripts\activate` (no Windows)
4.  Instale as dependências: `pip install -r requirements.txt`
5.  Aplique as migrações do banco de dados: `python manage.py migrate`
6.  Inicie o servidor: `python manage.py runserver`
7.  **Deixe este terminal aberto.** O servidor precisa ficar rodando para o app funcionar.

---

## Passo 3: Compilando e Rodando o App Android

1.  Abra o **Android Studio**.
2.  Clique em **Open** e navegue até a pasta `CondoManager` que você clonou.
3.  Aguarde o Android Studio sincronizar o projeto. Isso pode levar alguns minutos.
4.  Crie um emulador de dispositivo Android, se ainda não tiver um (**Tools > Device Manager**).
5.  Selecione o emulador e clique no botão **Run 'app'** (ícone de play verde).

---

## Passo 4: Plano de Testes Manuais (UAT)

Com o backend rodando e o app aberto no emulador, siga estes cenários:

### Cenário 1: Registro e Login
1.  Na tela de login, clique no link "Registre-se aqui."
2.  Cadastre um novo usuário (ex: `Nome: Teste UAT`, `Contato: uat@teste.com`, `Senha: 123`).
3.  Após o sucesso, na tela de login, entre com as credenciais `uat@teste.com` e `senha123`.
- **Resultado Esperado:** Login bem-sucedido, você é levado para a tela principal.

### Cenário 2: Criar Comunicado
1.  Na tela principal, clique no botão `+`.
2.  Crie um comunicado de emergência.
- **Resultado Esperado:** O comunicado aparece na lista e uma notificação do sistema é exibida.

### Cenário 3: Encerrar Comunicado (Como Gestor)
1.  Feche o app. No terminal onde o backend está rodando, pare o servidor (Ctrl+C).
2.  Crie um usuário gestor: `python manage.py createsuperuser`. Siga os prompts para criar um admin.
3.  Inicie o servidor novamente: `python manage.py runserver`.
4.  No app, faça login com o superusuário que você criou.
5.  Abra um comunicado e clique em "Encerrar Comunicado".
- **Resultado Esperado:** O comunicado é marcado como encerrado e não é mais possível adicionar complementos.

---

## Passo 5: Reportando os Resultados

Após concluir os testes, por favor, me informe se tudo ocorreu como esperado ou se encontrou algum problema. Um "tudo certo!" é o que esperamos ouvir.

Obrigado pela colaboração!
