FROM openjdk:24-jdk
LABEL maintainer="russelquinto.official@gmail.com"
WORKDIR /server
COPY ./target/*.jar rental-gadget-server.jar
EXPOSE 8080
# ENTRYPOINT [ "java", "-jar", "rental-gadget-server.jar", "--spring.profiles.active=prod" ]
CMD sleep 30 && java -jar rental-gadget-server.jar --spring.profiles.active=prod
