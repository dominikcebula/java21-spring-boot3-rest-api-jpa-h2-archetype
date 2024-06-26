[![Java 21 Spring Boot 3 REST API with JPA Archetype GitHub Actions CI Pipeline](https://github.com/dominikcebula/java21-spring-boot3-rest-api-jpa-h2-archetype/actions/workflows/maven.yml/badge.svg)](https://github.com/dominikcebula/java21-spring-boot3-rest-api-jpa-h2-archetype/actions/workflows/maven.yml)

# Java 21 Spring Boot 3 REST API with JPA Archetype

## Intro

This repository contains a Maven Archetype that can be used to generate a Java 21 Spring Boot 3 REST API project with
JPA.

Included example code will create a Spring Boot 3 REST API with JPA Application:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.2)

...
2023-02-07T22:06:26.555+01:00  INFO 9308 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2023-02-07T22:06:26.587+01:00  INFO 9308 --- [           main] com.dominikcebula.Application            : Started Application in 4.083 seconds (process running for 4.816)
```

Generated application will include:

* Swagger UI under http://localhost:8080/swagger-ui/index.html
* OpenAPI under http://localhost:8080/api-docs
* H2 Console under http://localhost:8080/h2-console
* Example REST API under http://localhost:8080/api/movies

## Usage

Invoke command:

```
$ mvn archetype:generate -DarchetypeGroupId=com.dominikcebula.archetypes -DarchetypeArtifactId=java21-spring-boot3-rest-api-jpa-h2-archetype
```

Maven Archetype will ask about `groupId`, `artifactId`, `version`, `package name` and will generate a project skeleton.

## Generated project

Having the project generated, invoke:

```
$ mvn clean install
```

Executable jar with all dependencies will be generated under `target` folder.

You can execute generated `jar` using command:

```
$ java -jar target/generated-output-1.0-SNAPSHOT.jar
```

Example endpoint can be used with following request:

```
$ curl http://localhost:8080/api/movies
[
  {
    "id": 1,
    "title": "The Godfather",
    "releaseYear": 1972
  },
  {
    "id": 2,
    "title": "Casablanca",
    "releaseYear": 1942
  },
  {
    "id": 3,
    "title": "The Shawshank Redemption",
    "releaseYear": 1994
  },
  {
    "id": 4,
    "title": "Gone with the Wind",
    "releaseYear": 1939
  },
  {
    "id": 5,
    "title": "Psycho",
    "releaseYear": 1960
  }
]
```
