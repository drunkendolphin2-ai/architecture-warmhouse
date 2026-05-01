# Стадия 1: сборка
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Стадия 2: рантайм
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/target/app.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]