FROM openjdk:11-jdk-slim AS builder
WORKDIR /src
ADD . /src
RUN ./gradlew clean assemble

FROM openjdk:11-jre-slim
RUN addgroup --system appgroup && useradd --system --no-create-home -g appgroup appuser
WORKDIR /app
COPY --from=builder /src/build/libs/*-0.0.1-SNAPSHOT.jar .
USER appuser
CMD exec java $JAVA_OPTS -jar *.jar
HEALTHCHECK --interval=10s --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
