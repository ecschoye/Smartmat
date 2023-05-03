FROM maven:3.8.5-openjdk-18 AS build
WORKDIR /app
COPY pom.xml .
#grocery list
COPY matvareliste.csv .
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests
FROM openjdk:18-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/matvareliste.csv .
ENV SPRING_PROFILES_ACTIVE=prod MY_PORT=8080
EXPOSE ${MY_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]