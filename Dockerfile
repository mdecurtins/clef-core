FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/core

RUN mkdir -p /usr/src/algorithms

RUN mkdir -p /usr/local/dtds

COPY ./clef/target/$JARFILE /usr/src/core/$JARFILE

RUN apk --no-cache add curl

WORKDIR /usr/src/core

CMD java $JAVA_OPTIONS -jar ./$JARFILE
