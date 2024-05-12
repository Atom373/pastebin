FROM maven:3.6-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

ENV APP_SERVER_URL=http://app

COPY --from=build /app/target/*.jar application.jar

COPY credentials /app/

COPY config /app/

EXPOSE 8080

CMD java -jar application.jar