FROM openjdk:11.0.6-jdk-slim
ADD startup.sh ./startup.sh
ADD target/example-1.1.0-SNAPSHOT.jar ./pacsystem.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ENTRYPOINT  ["./startup.sh"]
