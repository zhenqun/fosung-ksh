ARG JAR_BASE
FROM ${JAR_BASE}
MAINTAINER wangyihua <wangyh@qq.com>

ARG JAR_FILE
ADD ${JAR_FILE} /home/project/app.jar

RUN mkdir -p /data/logs/ksh-eureka
VOLUME /data/logs/ksh-eureka
#ADD supervisord_gateway_web.conf /etc/supervisor.conf.d/
