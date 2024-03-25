FROM alpine/git as clone
WORKDIR /app

ARG CORE_REPO_URL
RUN git clone -b main $CORE_REPO_URL

FROM jelastic/maven:3.9.5-openjdk-21 as build
WORKDIR /app
COPY --from=clone /app/onlinestore/ /app/
#RUN mvn formatter:format
RUN mvn package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar /app/
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar *.jar"]