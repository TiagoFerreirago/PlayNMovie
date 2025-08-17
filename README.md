# PlayNMovie

PlayNMovie é uma API REST desenvolvida em Java com Spring Boot para gerenciamento de um catálogo de filmes e jogos, permitindo que usuários autenticados possam favoritar itens e escrever resenhas.

## Funcionalidades

- Cadastro, consulta, atualização e remoção de filmes e jogos
- Favoritar/desfavoritar filmes e jogos por usuário autenticado
- Adição, edição e remoção de resenhas (reviews) por usuário autenticado
- Integração com APIs externas (TMDb para filmes, IGDB para jogos)
- Autenticação JWT e controle de acesso por usuário

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA
- Spring Security (JWT)
- MySQL
- Maven

## Estrutura do Projeto

```
src/
  main/
    java/
      com/th/playnmovie/
        controller/      # Controllers REST
        dto/             # Data Transfer Objects
        exception/       # Exceptions customizadas
        mapper/          # Conversores entre entidades e DTOs
        model/           # Entidades JPA
        repository/      # Repositórios JPA
        security/        # Autenticação e autorização
        service/         # Regras de negócio
        util/            # Utilitários
    resources/
      application.properties
  test/
    java/
      com/th/playnmovie/
        mock/            # Mocks para testes
        service/         # Testes de serviços
```

## Como executar

1. Configure o banco de dados MySQL em `src/main/resources/application.properties`.
2. Instale as dependências:
   ```
   mvn clean install
   ```
3. Execute a aplicação:
   ```
   mvn spring-boot:run
   ```

## Autenticação

A API utiliza JWT para autenticação. Para acessar endpoints protegidos, obtenha um token via `/auth/signin` e envie-o no header `Authorization: Bearer <token>`.

## Licença

MIT License

---

Projeto pessoal para portfólio. Sinta-se à vontade para contribuir ou utilizar como