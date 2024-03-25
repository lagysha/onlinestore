FROM eclipse-temurin:21

WORKDIR /app

EXPOSE 8080

COPY target/onlinestore-0.0.1-SNAPSHOT.jar /app/onlinestore.jar

ENTRYPOINT ["java", "-jar", "onlinestore.jar"]
