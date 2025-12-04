FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -e -X -DskipTests clean package

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/Garden-Project-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]


