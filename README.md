# Desafio 3 - Davidson Aguiar

## Descrição do Projeto

Este projeto consiste em uma aplicação backend para um e-commerce, desenvolvida como parte do Desafio 3 da trilha de estudos da bolsa oferecida pela UOL Compass com AWS. A aplicação permite a criação, gerenciamento de produtos, controle de estoque, gerenciamento de vendas e usuários, seguindo as melhores práticas de desenvolvimento de software.

## Tecnologias Utilizadas

- **Linguagem**: Java 22
- **Framework**: Spring Boot
- **Banco de Dados**: PostgreSQL
- **Arquitetura de API**: Clean Architecture
- **Autenticação e Autorização**: JWT (JSON Web Token)

## Requisitos

1. **Gestão de Produtos**:
    - CRUD (Create, Read, Update, Delete) completo para produtos.
    - Validações como preço positivo e inativação de produtos que já foram incluídos em uma venda.

2. **Controle de Estoque**:
    - Verificação do estoque antes da conclusão de uma venda, impedindo a venda de produtos com estoque insuficiente.

3. **Gestão de Vendas**:
    - CRUD completo para vendas, garantindo que uma venda tenha pelo menos um produto para ser concluída.

4. **Relatórios de Vendas**:
    - Relatórios de vendas por data, por mês e pela semana atual (considerando dias úteis).

5. **Cache**:
    - Implementação de cache para as buscas de produtos e vendas, com gerenciamento eficiente para garantir dados sempre atualizados.

6. **Tratamento de Exceções**:
    - Tratamento de todas as exceções seguindo um padrão de resposta unificado.

7. **Padrão de Datas**:
    - Todas as datas seguem o padrão ISO 8601.

8. **Autenticação e Autorização**:
    - Implementação de autenticação via JWT.
    - Implementação de autorização, com permissões específicas para usuários ADMIN.

9. **Reset de Senha**:
    - Método para permitir que os usuários possam resetar suas senhas.

10. **Permissões de ADMIN**:
    - Somente usuários ADMIN podem deletar informações, cadastrar e atualizar produtos e outros usuários ADMIN.

## Diagrama ER/UML

[**Clique aqui**](https://lucid.app/lucidchart/b25beb49-2869-4ef9-99ae-3f038fc519a2/edit?viewport_loc=510%2C181%2C647%2C1111%2C0_0&invitationId=inv_82c6eea9-6755-49b6-853d-74a33d429a5a) para ser redirecionado para o diagrama ER/UML do projeto no Lucidchart.
