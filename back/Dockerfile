FROM openjdk:11.0.6-jdk-slim
ADD startup.sh ./startup.sh
ADD target/example-1.1.0-SNAPSHOT.jar ./pacsystem.jar

ENTRYPOINT  ["./startup.sh"]
