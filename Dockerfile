# build
FROM maven:3.9.3-eclipse-temurin-17 AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# run
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build target/*.jar app.jar

# Copia configurazione firebase
COPY firebase-config /app/firebase-config

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
