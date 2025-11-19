FROM node:alpine
WORKDIR /website
COPY package.json ./
RUN npm install
COPY . .
CMD ["npm", "run", "dev"]
