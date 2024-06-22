FROM alpine/git as clone
WORKDIR /app
ARG BRANCH
RUN git clone -b $BRANCH https://github.com/lagysha/onlinestore

FROM maven:3.9-amazoncorretto-21 as build
WORKDIR /app
COPY --from=clone /app/onlinestore /app
RUN mvn package -DskipTests

FROM openjdk:22-ea-21-jdk-slim
WORKDIR /app
COPY --from=build /app/core/target/*.jar /app
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar *.jar"]