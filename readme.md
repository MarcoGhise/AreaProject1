## Area Project 1

An application built to insert unstructured data inside a NoSQL database and retrieve that through a full text index search.

A scheduled task runs to ingest data from external resource.

## Index

1. [Architectural view](#Architecturalview)
2. [Quick Start](#QuickStart)
3. [Mongo](#mongo)
5. [RabbitMQ](#rabbitmq)
6. [Eureka Registry](#EurekaRegistry)
7. [Front End](#FrontEnd)
8. [Ingestor](#Ingestor)
9. [Storage](#Storage)
10. [Scheduler](#Scheduler)
11. [Metrics](#Metrics)

## Application Stack

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

## Architectural view

![Architecture Diagram](https://github.com/MarcoGhise/AreaProject1/blob/main/img/arch.jpg)

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

## Mongo
A collection called "information" inside "test" database is created in Mongo Db during Docker container start up.

Data structure is the follow:
```
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "_id": {
      "type": "string"
    },
    "type": {
      "type": "string"
    },
    "payload": {
      "type": "object"
    },
    "dtInsert": {
      "type": "string"
    },
    "_class": {
      "type": "string"
    }
  },
  "required": [
    "_id",
    "type",
    "payload",
    "dtInsert",
    "_class"
  ]
}
```

Payload node contains whatever data inserted.

## RabbitMQ
RabbitMQ message broker decouple communications between Ingestor/Scheduler services and Storage.
Dashboard is available at http://localhost:15672 (username: info, password: news).

A single Queue "notify" is configured and joined at direct exchange binding called "notify-exchange".
The queue is not persistable.

![RabbitMQ dashboard](https://github.com/MarcoGhise/AreaProject1/blob/main/img/rabbitmq.jpg)

## Eureka Registry
Services register themselves into Discovery Registry in order to discovery each other without hard coding IP address and/or port.
Also, Registry checks their health status and put service offline when is not available.

## Front End
Front end service using Spring Boot Framework (2.7) and Thymeleaf template to build a Http Web Application available on port 80. 

The application is available through basic authentication (username: admin, password: password). 

The site is based on two page: In the "Insert" page, a user can add an information with specified a Kind.

![Insert page](https://github.com/MarcoGhise/AreaProject1/blob/main/img/insert.JPG)

In the "Search" page, a user can look for any type of word from information ingested into NoSQL database.

![Search Result](https://github.com/MarcoGhise/AreaProject1/blob/main/img/search.JPG)

## Ingestor
Ingestor service is not exposed on public port and get data from FrontEnd in order to transform it in a message.
The message is sent to a RabbitMQ message broker.
A retry policy is configured in order to avoid lost message.
```
cache:
  channel:
    #Number of channels to retain in the cache. When "check-timeout" > 0, max channels per connection.
    size: 2
    #Duration to wait to obtain a channel if the cache size has been reached. If 0, always create a new channel.
    checkout-timeout: 10000
```
## Storage
Storage service is not exposed on public port and make available two feature.
First of all, it works as listener to get data from RabbitMQ and store into NoSQL.
It also makes available an endpoint to get full text search from MongoDB.

Data is cached when the full text search endpoint is called (/information/{word}).
{word} is the cache key.
All entries in cache are evicted when new information is added.

A retry policy is configured in order to avoid lost message.

```
listener:
  simple:
    retry:
      enabled: true
      initial-interval: 3s
      max-attempts: 3
      max-interval: 10s
      multiplier: 2
```

## Scheduler   
Scheduler service works as job executor. Get news from BBC feed
```
http://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=9acc642023684f07b46fae89185513ce
```
Any entry generates a message sent to RabbitMQ message broker to put them into NoSQL.

## Metrics 

FrontEnd service makes metrics available using Micrometer with Prometheus adapter.
Prometheus requests application metrics from FrontEnd service in order to make it available to Grafana.

Prometheus is available at endpoint http://localhost:9090

Grafana is available at endpoint http://localhost:3000 (user: admin, password: password)

![FrontEnd Grafana](https://github.com/MarcoGhise/AreaProject1/blob/main/img/grafana.jpg)

Data is scraped every 40 seconds.
