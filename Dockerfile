FROM azul/zulu-openjdk:11 AS builder
WORKDIR /src
ADD . /src
RUN ./gradlew clean assemble

FROM mcr.microsoft.com/java/jre-headless:11u2-zulu-alpine
RUN addgroup -S appgroup && adduser -D -H -S appuser -G appgroup -s /sbin/nologin
WORKDIR /app
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar .
USER appuser
CMD exec java $JAVA_OPTS -jar *.jar
HEALTHCHECK --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
