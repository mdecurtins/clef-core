FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/core

RUN mkdir -p /usr/src/algorithms

COPY ./clef/target/$JARFILE /usr/src/core/$JARFILE
COPY ./test.sh /usr/src/core/test.sh

RUN apk --no-cache add curl

WORKDIR /usr/src/core

#CMD [ "/bin/sh", "-c", "java", "$JAVA_OPTIONS", "-jar", "$JARFILE" ]
CMD java $JAVA_OPTIONS -jar ./$JARFILE
#CMD [ "/bin/sh", "-c", "./test.sh" ]