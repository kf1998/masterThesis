# Build project
FROM maven:3-openjdk-17 AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml ./
RUN mvn -f pom.xml -DskipTests clean package

# Create jar
FROM openjdk:17-jdk
COPY --from=build /home/app/target/user-0.0.1-SNAPSHOT.jar /usr/local/lib/user.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/usr/local/lib/user.jar"]