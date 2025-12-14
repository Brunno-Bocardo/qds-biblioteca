# Biblioteca - Qualidade de Software

---

## 1. Descrição

...

---

## 2. Funcionalidades

...

---

## 3. Tecnologias

* Java 17+  
* Spring Boot 3+  
* Spring Data JPA  
* H2 Database  
* Swagger/OpenAPI  
* Maven

---

## 4. Endpoints

* **Swagger UI:**  
  [http://localhost:8080/demo/v1/swagger-ui/index.html#/](http://localhost:8080/demo/v1/swagger-ui/index.html#/)

* **H2 Console:**  
  [http://localhost:8080/demo/v1/h2-console/login.do](http://localhost:8080/demo/v1/h2-console/login.do)  

  * **JDBC URL:** `jdbc:h2:file:~/bibliotecaDB`  
  * **Usuário:** `sa`  
  * **Senha:** (deixe em branco)

---

## 5. Como Rodar o Projeto

1.  Abra o projeto no IDE de sua preferência (Eclipse, IntelliJ, VS Code).
2.  Execute a aplicação pelo terminal da IDE ou diretamente no terminal do projeto:

    ```bash
    mvn spring-boot:run
    ```

3.  Teste os endpoints via **Swagger UI** ou usando ferramentas como **Postman** ou **curl**.
4.  Para acessar o **H2 Console** (banco de dados em memória):
    *   **URL:** [http://localhost:8080/demo/v1/h2-console](http://localhost:8080/demo/v1/h2-console)
    *   **JDBC URL:** `jdbc:h2:file:~/bibliotecaDB`
    *   **Usuário:** `sa`
    *   **Senha:** *(deixe em branco)*

---

## 6. Observações

*   O banco **H2** é em memória, portanto os dados são apagados a cada reinício da aplicação.
*   O projeto contém **código intencionalmente bagunçado**, permitindo a prática de **refatoração e Clean Code**.
*   Os alunos podem aplicar boas práticas de:
    *   API REST
    *   Tratamento de exceções
    *   Uso de DTOs (Data Transfer Objects)
