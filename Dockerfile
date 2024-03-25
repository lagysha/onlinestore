FROM alpine/git as clone
WORKDIR /app

RUN git clone -b main https://github.com/lagysha/onlinestore

FROM jelastic/maven:3.9.5-openjdk-21 as build
WORKDIR /app
COPY --from=clone /app/onlinestore/ /app/
EXPOSE 8080
RUN mvn package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar /app/
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar *.jar"]