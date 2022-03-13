# Teste CVC para Java Developer Backend

### Arquitetura
- Java 8
- Spring Boot
- Maven 
- Eclipse

### Instruções do Eclipse
- De uma IDE Java importar esse projeto desse repositório
- Se já tiver o plugin do Spring instalado, basta rodar o projeto como Spring Boot App (botão direito do mouse no projeto, Run As -> Sprint Boot App)
- Caso negativo, executar com o Maven (botão direito do mouse no projeto, Run As -> Maven build... -> Goals: spring-boot:run)

### Instruções do Maven
- Através de um shell (linux) ou cmd/PowerShell (windows), acessar o diretório do projeto
- Executar o comando: $ mvn spring-boot:run

### Teste 1
http://localhost:8080/booking/9626/2019-09-24/2019-09-30/1/0, aonde:
- 9626: código da cidade
- 2019-09-24: checkin
- 2019-09-30: checkout
- 1: qtde de adultos
- 0: qtde de crianças

### Teste 2
http://localhost:8080/hotel/6, aonde:
- 6: ID do hotel
