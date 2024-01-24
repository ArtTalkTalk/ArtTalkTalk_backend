# Set the base image to use, in this case, adoptopenjdk with Java 17
FROM adoptopenjdk:17-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY build/libs/youth_be-0.0.1-SNAPSHOT.jar /app/

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "/app/youth_be-0.0.1-SNAPSHOT.jar"]
