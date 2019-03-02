FROM openjdk:11-jdk-slim AS builder
WORKDIR /src
ADD . /src
RUN ./gradlew clean assemble

FROM openjdk:11-jdk-slim AS jre
WORKDIR /app
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar .
RUN jlink --output ./jre --add-modules $(jdeps --print-module-deps *-0.0.1-SNAPSHOT.jar),java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument

FROM debian:9-slim
RUN addgroup --system appgroup && useradd --system --no-create-home -g appgroup appuser
WORKDIR /app
COPY --from=jre /app/jre ./jre
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar .
USER appuser
CMD exec ./jre/bin/java $JAVA_OPTS -jar *.jar
HEALTHCHECK --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
