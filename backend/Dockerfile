FROM eclipse-temurin:24
LABEL maintainer="russelquinto.official@gmail.com"
RUN apt-get update && apt-get install -y mariadb-backup
WORKDIR /server
COPY ./target/*.jar rental-gadget-server.jar
# ENTRYPOINT [ "java", "-jar", "rental-gadget-server.jar", "--spring.profiles.active=prod" ]
CMD sleep 30 && java -jar rental-gadget-server.jar --spring.profiles.active=prod
