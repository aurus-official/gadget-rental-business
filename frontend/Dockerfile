FROM node:alpine
WORKDIR /website
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build
