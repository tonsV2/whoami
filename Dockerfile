FROM openjdk:8-jdk-alpine as builder
WORKDIR /src
ADD . /src
RUN ./gradlew clean assemble

FROM openjdk:8-jre-alpine
RUN addgroup -S appgroup && adduser -D -H -S appuser -G appgroup -s /sbin/nologin
WORKDIR /app
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar .
USER appuser
CMD exec java -Xmx512M -Xms256M -jar *.jar
HEALTHCHECK --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
