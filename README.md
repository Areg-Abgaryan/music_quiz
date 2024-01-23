Music Quiz Console Application

Overview

Welcome to the Music Quiz Application!
This Java console application is designed to provide users with an interactive music quiz experience.
Users can test their knowledge about music albums, artists, and songs in a fun and engaging way.

The application leverages PostgreSQL for data storage, Spring Boot for the backend and Spring Data JPA for database interaction.


Prerequisites :

Before running the application, ensure that the following are installed :

Java Development Kit (JDK) [ This application requires Java 17 or later ],
Gradle,
PostgreSQL Database


-> Update the database configuration in application.properties with your database details :
spring.datasource.url,
spring.datasource.username,
spring.datasource.password

-> Do not forget to config your mail sending server and set these two properties in application.properties file :
spring.mail.username,
spring.mail.password

-> Build the project with Gradle :
./gradlew clean build
