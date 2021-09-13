# CO2 Sensor Metrics App

The App is capable of collecting data from hundreds of thousands of sensors and alert if the CO2 concentrations reach critical levels.

## Swagger UI

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Requirements

For building and running the application you need:

- [JDK 1.8](openjdk version "1.8.0_41")
- [Maven 3.6](https://maven.apache.org)

#### Environment variables required
 
 |     ENV Variable              | Req? |    Values      |     Default           |            Notes             |
 | ------------------------      | ---- | -------------- | --------------------- | ---------------------------- |
 | DATABASE_URI                  |  Y   | string         | mongodb://localhost:27017/alertingdb | Mongo host where mongo is configured. |

	   
			   
## Technology Stack
Technology Stack contains following technologies/frameworks:
1. Spring Boot
2. lombok
3. H2 Database Engine
4. Maven
5. Log4j2
7. Bucket4j - Rate limiting library

## Installation and Getting Started

### Prerequisites
1. JDK 1.8
2. Maven 3.0+
3. Eclipse or IntelliJ IDE
4. Lombok plugin installed for your IDE.
<br/>
Intellij - [Link] https://projectlombok.org/setup/intellij
<br/>
Eclipse - [Link] https://projectlombok.org/setup/eclipse

## Run App

Step-1 : Checkout the project : 

`git clone https://stash.dev-charter.net/stash/scm/vpns/as.git`

Step-2 : Change to directory as

`cd as`

Step-4 : Clean and build the project with Maven

`mvn clean install`

Step-5 : Run it as a spring boot app

`mvn spring-boot:run`