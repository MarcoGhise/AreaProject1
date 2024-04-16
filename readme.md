## Area Project 1

An application built to insert unstructured data inside a NoSQL database and retrieve that through a full text index search.

A scheduled task runs to ingest data from external resource.

### Application Stack

- Spring Boot 2.7
- Thymeleaf template
- Docker
- Spring Data Mongo
- Eureka Register
- RabbiMQ
- Prometheus
- Grafana
- EhCache
- Micrometer

### Architectural view

[Picture]

## Quick Start
Run maven root project (./pom.xml) with Jdk 17
```
set JAVA_HOME=c:\Program Files\Java\jdk-17.0.3.1
c:\apache-maven-3.6.3\bin\mvn clean package -DskipTests
```
Start up the containers by running
```
docker-compose up -d
```

### Eureka Registry
Services register themselves into Discovery Registry in order to discovery each other without hard coding IP address and/or port.
Also, Registry checks their health status and put service offline when is not available.

### Front End
Front end service using Spring Boot Framework (2.7) and Thymeleaf template to build a Http Web Application available on port 80. 

The application is available through basic authentication (username: admin, password: password). 

The site is based on two page: In the "Insert" page, a user can add an information with specified a Kind.
In the "Search" page, a user can look for any type of word from information ingested into NoSQL database.

### Ingestor
Ingestor service is not exposed on public port and get data from FrontEnd in order to transform it in a message.
The message is sent to a RabbitMQ message broker.

## Storage
Storage service is not exposed on public port and make available two feature.
First of all, it works as listener to get data from RabbitMQ and store into NoSQL.
It also makes available an endpoint to get full text search from MongoDB.

Data is cached when the full text search endpoint is called (/information/{word}).
{word} is the cache key.
All entries in cache are evicted when new information is added.  

## Scheduler   
Scheduler service works as job executor. Get news from BBC feed
```
http://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=9acc642023684f07b46fae89185513ce
```
And send a message to RabbitMQ message broker to put them into NoSQL.

## Metrics 

FrontEnd service make metrics available using Micrometer with Prometheus adapter.
Prometheus requests application metrics from FrontEnd service in order to make it available to Grafana.

Prometheus is available at endpoint http://localhost:9090

Grafana is available at endpoint http://localhost:3000 (user: admin, password: password)

Data is scraped every 40 seconds.