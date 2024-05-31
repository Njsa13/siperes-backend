FROM openjdk:17.0.9-jdk-oracle
VOLUME /tmp
ADD target/siperes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]