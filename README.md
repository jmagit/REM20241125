# Curso de Microservicios con Spring

## Instalación

- [JDK](https://www.oracle.com/java/technologies/downloads/)
- [Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2024-09/R/eclipse-jee-2024-09-R-win32-x86_64.zip)
  - Help > Eclipse Marketplace ... > [Spring Tools 4 (aka Spring Tool Suite 4)](https://marketplace.eclipse.org/content/spring-tools-4-aka-spring-tool-suite-4)
  - [Project Lombok](https://projectlombok.org/downloads/lombok.jar)
- Clientes de bases de datos (opcionales)
  - [HeidiSQL](https://www.heidisql.com/download.php)
  - [MongoDB Compass](https://www.mongodb.com/try/download/compass)

## Paquetes Java (descargar y descomprimir)

- <https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-5.1.49.zip>
- <https://www.eclipse.org/downloads/download.php?file=/rt/eclipselink/releases/4.0.4/eclipselink-4.0.4.v20240715-059428cdd2.zip>

## Base de datos de ejemplos

- [Página principal Sakila](https://dev.mysql.com/doc/sakila/en/)
- [Diagrama de la BD Sakila](http://trifulcas.com/wp-content/uploads/2018/03/sakila-er.png)

## Contenedores

### Instalación Docker

- <https://learn.microsoft.com/es-es/windows/wsl/install>
- <https://docs.docker.com/desktop/install/windows-install/>

### Bases de datos

#### MySQL

    docker run -d --name mysql-sakila -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 jamarton/mysql-sakila

#### MongoDB

    docker run -d --name mongodb -p 27017:27017 -v .:/externo jamarton/mongodb-contactos

#### Redis

    docker run -d --name redis -p 6379:6379 -p 6380:8001 -v .:/data redis/redis-stack:latest

### Agentes de Mensajería

#### Comandos: RabbitMQ (AMQP)

    docker run -d --name rabbitmq -p 4369:4369 -p 5671:5671 -p 5672:5672 -p 15691:15691 -p 15692:15692 -p 25672:25672 -p 15671:15671 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=curso rabbitmq:management-alpine    

#### Eventos: Kafka (docker compose)

Comando:

    cd docker\kafka && docker compose up -d

#### Apache ActiveMQ o Artemis (JMS)

    docker run -d --name activemq-classic -p 1883:1883 -p 5672:5672 -p 8161:8161 -p 61613:61613 -p 61614:61614 -p 61616:61616 apache/activemq-classic

    docker run -d --name activemq-artemis -p 1883:1883 -p 5445:5445 -p 5672:5672 -p 8161:8161 -p 9404:9404 -p 61613:61613 -p 61616:61616 apache/activemq-artemis:latest-alpine

### Monitorización, supervisión y trazabilidad

#### Monitorización: Prometheus con Grafana (docker compose)

Comando:

    cd docker\prometheus && docker compose up -d

#### Supervisión: ELK

Comando:

      cd docker\elk && docker compose up -d

#### Trazabilidad: Zipkin

    docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin-slim

## Documentación

- <https://docs.spring.io/spring-framework/reference/>
- <https://docs.spring.io/spring-boot/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/commons/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/jpa/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/>
- <https://docs.spring.io/spring-data/redis/docs/current/reference/html/>
- <https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web>
- <https://docs.spring.io/spring-data/rest/docs/current/reference/html/>
- <https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer>
- <https://docs.spring.io/spring-cloud-config/docs/current/reference/html/>
- <https://docs.spring.io/spring-security/reference/index.html>

## Ejemplos

- <https://github.com/rabbitmq/rabbitmq-tutorials/tree/main/spring-amqp>
- <https://github.com/spring-projects/spring-amqp-samples>
- <https://github.com/spring-projects/spring-kafka/tree/main/samples>
- <https://github.com/spring-projects/spring-petclinic>
- <https://github.com/spring-projects/spring-data-examples>
- <https://github.com/spring-projects/spring-data-rest-webmvc>
- <https://github.com/spring-projects/spring-hateoas-examples>

## Laboratorios

- [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)
- [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq)
- [Messaging with JMS](https://spring.io/guides/gs/messaging-jms)
- [Messaging with Redis](https://spring.io/guides/gs/messaging-redis)
- [Spring Cloud Stream](https://spring.io/guides/gs/spring-cloud-stream)
- [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket)
- [Managing Transactions](https://spring.io/guides/gs/managing-transactions)
