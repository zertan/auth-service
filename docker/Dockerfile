# FROM clojure as builder

# COPY . .

# RUN lein uberjar

#FROM openjdk:8-jre-alpine

FROM clojure:alpine

ENV NAME=authsvc
ENV VERSION=0.1.0-SNAPSHOT

COPY ${NAME}-${VERSION}-standalone.jar .
ENTRYPOINT java -jar ${NAME}-${VERSION}-standalone.jar
EXPOSE 7890
EXPOSE 8080
