FROM maven:3.9.4 as builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-oracle

COPY --from=builder /app/target/siperes-*.jar /siperes.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/siperes.jar"]