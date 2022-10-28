FROM openjdk:11-jdk-slim
VOLUME /tmp
EXPOSE 8080
WORKDIR /startup/
ADD target/ms-generator-service-0.0.1-SNAPSHOT.jar /startup/ms-generator-service.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/startup/ms-generator-service.jar"]