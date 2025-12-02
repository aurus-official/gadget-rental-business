#!/bin/bash

cd ~/Programming/gadget-rental/backend
mvn clean
mvn verify
docker build . -t backend

cd ~/Programming/gadget-rental/frontend/
npm run build
docker build . -t frontend

cd ~/Programming/gadget-rental/nginx/
docker build . -t nginx 

cd ~/Programming/gadget-rental/
docker-compose up

