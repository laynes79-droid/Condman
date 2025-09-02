# Instruções para Teste de Aceitação do Usuário (UAT) - CondoCare

Olá! Este documento irá guiá-lo através dos passos para testar a aplicação CondoCare. O objetivo é validar todas as funcionalidades principais do ponto de vista de um usuário final.

## Passo 1: Preparar o Ambiente

Antes de começar, precisamos garantir que tanto o servidor de backend quanto a aplicação Android estejam rodando.

### 1.1. Rodar o Servidor Backend (Django)

1.  Abra um terminal ou prompt de comando.
2.  Navegue até a pasta do projeto.
3.  Ative o ambiente virtual (se ainda não estiver ativo):
    -   **macOS/Linux**: `source venv/bin/activate`
    -   **Windows**: `venv\Scripts\activate`
4.  Inicie o servidor:
    ```bash
    python manage.py runserver
    ```
    Você deverá ver o servidor rodando. Mantenha este terminal aberto.

### 1.2. Rodar a Aplicação (Android)

1.  Abra o projeto no Android Studio.
2.  Espere o Gradle sincronizar.
3.  Inicie a aplicação em um emulador ou dispositivo físico clicando no botão "Run" (ícone de play verde).

## Passo 2: Teste de Fluxo - Cenários de Usuário

Vamos simular o uso da aplicação por dois tipos de usuários: **Manager (Síndico)** e **Resident (Morador)**.

### Cenário A: O Síndico (Manager)

#### A1. Registrar uma conta de Síndico

1.  Na tela de login, clique no link **"Don't have an account? Register here."**.
2.  Preencha o formulário de registro:
    -   **Name**: `Síndico Teste`
    -   **Email**: `sindico@teste.com`
    -   **Password**: `senha123`
    -   **Apartment**: `Admin`
    -   **Role**: Selecione **Manager** no spinner.
3.  Clique no botão **"Register"**.
4.  Você deverá ver uma mensagem de "Registration successful!" e ser redirecionado para a tela de login.

#### A2. Criar um novo comunicado

1.  Faça login com a conta do síndico que você acabou de criar (`sindico@teste.com` / `senha123`).
2.  Na tela principal, você deverá ver um botão flutuante de **"+"** no canto inferior direito. Clique nele.
3.  Preencha o formulário do comunicado:
    -   **Title**: `Teste de Manutenção`
    -   **Message**: `O elevador estará em manutenção amanhã.`
    -   **Emergency**: Deixe o switch desligado.
4.  Clique no botão **"Create"**.
5.  Você deverá ser redirecionado para a tela principal, e o novo comunicado **"Teste de Manutenção"** deve aparecer na lista.

### Cenário B: O Morador (Resident)

#### B1. Registrar uma conta de Morador

1.  Se você ainda estiver logado como síndico, precisará fazer logout. (Nota: A função de logout não foi solicitada, então você pode precisar fechar e reabrir o app para voltar à tela de login).
2.  Na tela de login, clique para registrar uma nova conta.
3.  Preencha o formulário:
    -   **Name**: `Morador Teste`
    -   **Email**: `morador@teste.com`
    -   **Password**: `senha123`
    -   **Apartment**: `101`
    -   **Role**: Selecione **Resident** no spinner.
4.  Clique em **"Register"**.

#### B2. Visualizar comunicados e adicionar um complemento

1.  Faça login com a conta do morador (`morador@teste.com` / `senha123`).
2.  Na tela principal, você deverá ver o comunicado **"Teste de Manutenção"**. O botão de "+" não deve estar visível para moradores.
3.  Clique no comunicado **"Teste de Manutenção"** para ver os detalhes.
4.  Na tela de detalhes, no campo **"Add a complement"**, digite uma mensagem como `Obrigado por avisar!`.
5.  Clique no botão **"Add"**.
6.  O seu complemento deverá aparecer na lista de complementos abaixo, com seu nome e a data.

### Cenário C: O Síndico Encerra um Comunicado

#### C1. Encerrar o comunicado

1.  Faça login novamente com a conta do **Síndico**.
2.  Na tela principal, clique no comunicado **"Teste de Manutenção"**.
3.  Na tela de detalhes, você deverá ver um botão **"Close Communication"**. Clique nele.
4.  O status do comunicado deverá mudar para **"Status: Closed"**, e o botão para fechar o comunicado deverá desaparecer.

## Fim do Teste

Se todos os passos acima funcionaram como descrito, o teste de aceitação foi um sucesso! Obrigado por testar o CondoCare.
