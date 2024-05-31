FROM openjdk:17-jdk-oracle
VOLUME /tmp
ADD target/siperes-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]