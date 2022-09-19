# Build project
FROM maven:3-openjdk-17 AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml ./
RUN mvn -f pom.xml clean package

# Create jar
FROM openjdk:17-jdk
COPY --from=build /home/app/target/shop_mono-0.0.1-SNAPSHOT.jar /usr/local/lib/shop_mono.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/usr/local/lib/shop_mono.jar"]