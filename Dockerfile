FROM maven:3.9.4-openjdk-17-slim as builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM oracle/graalvm-ce:17.0.9-java17 as final

COPY --from=builder /app/target/siperes-*.jar /siperes.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/siperes.jar"]