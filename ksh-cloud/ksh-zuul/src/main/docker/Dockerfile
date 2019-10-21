ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER wangyihua <wangyh@fosung.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar

RUN mkdir -p /data/logs/ksh-zuul
VOLUME /data/logs/ksh-zuul

#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/
