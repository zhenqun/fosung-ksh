ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER liuzhenqun <liuzq@hd100.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar

RUN mkdir -p /data/logs/ksh-punsh
VOLUME /data/logs/ksh-punsh

#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/
