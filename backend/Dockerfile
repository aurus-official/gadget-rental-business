FROM openjdk:24-jdk
LABEL maintainer="russelquinto.official@gmail.com"
WORKDIR /app
COPY ./target/*.jar server.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "server.jar" ]

