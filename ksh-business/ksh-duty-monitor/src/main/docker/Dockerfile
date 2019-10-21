ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER lizhenqun <liuzq@hd100.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar

RUN mkdir -p /data/logs/ksh-duty-monitor
VOLUME /data/logs/ksh-duty-monitor

#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/
