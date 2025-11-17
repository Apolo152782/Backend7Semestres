FROM openjdk:17-jdk-slim
WORKDIR /app
COPY Proyecto5/target/Proyecto5-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
