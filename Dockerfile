FROM openjdk:8-jdk-alpine

RUN mkdir -p /usr/src/clef_core

ADD . /usr/src/clef_core

WORKDIR /usr/src/clef_core/clef/target

RUN pwd

#CMD [ "/bin/sh", "-c", "java", "$JAVA_OPTIONS", "-jar", "$JARFILE" ]
CMD java $JAVA_OPTIONS -jar $JARFILE
