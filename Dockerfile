FROM openjdk:8-jdk-alpine as builder
WORKDIR /src
ADD . /src
RUN ./gradlew clean assemble

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar /app/
CMD java -jar /app/*.jar
