FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/products-app-1.6.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
