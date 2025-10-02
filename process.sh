#!/bin/bash

cd ~/Programming/gadget-rental/backend
mvn clean
mvn verify

sudo docker build . -t rental-server
cd ..
sudo docker-compose up
