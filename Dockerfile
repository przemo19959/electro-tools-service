FROM amazoncorretto:25-alpine
COPY target/electro-tools-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]