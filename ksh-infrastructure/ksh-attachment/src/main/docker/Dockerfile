ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER wangyh <wangyh@fosung.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar
#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/

RUN mkdir -p /data/attachment
RUN mkdir -p /data/logs/attachment

VOLUME /data/attachment
VOLUME /data/logs/attachment
