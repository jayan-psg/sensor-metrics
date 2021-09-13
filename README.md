# CO2 Sensor Metrics App

The App is capable of collecting data from hundreds of thousands of sensors and alert if the CO2 concentrations reach critical levels.

## Swagger UI

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Requirements

For building and running the application you need:

- [JDK 1.8](openjdk version "1.8.0_41")
- [Maven 3.6](https://maven.apache.org)

#### Configuration variables required
 
 |     Property              | Req? |    Values      |     Default           |            Notes             |
 | ------------------------  | ---- | -------------- | --------------------- | ---------------------------- |
 | spring.datasource.url     |  Y   | string         | jdbc:h2:mem:testdb    | H2 database url              |
 | spring.datasource.username|  Y   | string         |   sa                  | Username                     |
 | spring.datasource.password|  Y   | string         |   password            | Password                     |
 | bucket4j.filters[0].url   |  Y   | string         | .*/mesurements        | Request url to which the rate limiting is applied.  1 request per min.
 | capacity                  |  Y   | number         |  1                    | No. of requests              |
 | time                      |  Y   | number         |  1                    | 1 min or 1 sec based on unit configured
 | unit                      |  Y   | string         |  minutes              | Time unit                    |
			   
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

`git clone https://github.com/jayan-psg/sensor-metrics.git

Step-2 : Clean and build the project with Maven

`mvn clean install`

Step-3 : Run it as a spring boot app

`mvn spring-boot:run`

##Notes:
1. Service should be able to receive measurements(POST /api/v1/sensors/{uuid}/mesurements) at a rate of 1 per min.  If hit more than 1 within a min, HTTP-429 Too Many Requests will be thrown. 
2. This solution can be improved by optimizing the things on database side using Timeseries DB - InfluxDB. 