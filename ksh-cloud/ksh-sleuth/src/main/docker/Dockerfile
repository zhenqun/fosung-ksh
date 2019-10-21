ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER wangyihua <wangyh@qq.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar

RUN mkdir -p /data/logs/ksh-sleuth
VOLUME /data/logs/ksh-sleuth
#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/
