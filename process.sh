#!/bin/bash

cd ~/Programming/gadget-rental/backend
mvn clean
mvn verify
docker build . -t rental-server

cd ~/Programming/gadget-rental/frontend/
docker build . -t rental-website

cd ~/Programming/gadget-rental/
docker-compose up
