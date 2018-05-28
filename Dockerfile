FROM anapsix/alpine-java:8_jdk_unlimited

MAINTAINER 'Vinlin Investment <xenkimoan@gmail.com'

VOLUME '/tmp'

ENV USER_NAME VINLIN
ENV APP_HOME /opt/$USER_NAME
#ENV APP_VERSION 0.1.0

RUN mkdir $APP_HOME
RUN adduser -S $USER_NAME

ADD build/libs/stockanalysis-*.jar $APP_HOME/stockanalysis.jar

RUN bash -c 'touch /stockanalysis.jar'
RUN chown $USER_NAME $APP_HOME/stockanalysis.jar

USER $USER_NAME

WORKDIR $APP_HOME
EXPOSE 8080

ENTRYPOINT [ "/bin/bash", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar stockanalysis.jar" ]
