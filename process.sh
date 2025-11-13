#!/bin/bash

cd ~/Programming/gadget-rental/backend
mvn clean
mvn verify

docker build . -t rental-server
cd ..
docker-compose up
