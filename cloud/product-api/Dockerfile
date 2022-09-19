# Build project
FROM maven:3-openjdk-17 AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml ./
RUN mvn -f pom.xml -DskipTests clean package

# Create jar
FROM openjdk:17-jdk
COPY --from=build /home/app/target/product-0.0.1-SNAPSHOT.jar /usr/local/lib/product.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/usr/local/lib/product.jar"]