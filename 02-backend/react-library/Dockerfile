FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/react-library-0.0.1-SNAPSHOT.jar app-library.jar

EXPOSE 8443
CMD ["java", "-jar","app-library.jar"]