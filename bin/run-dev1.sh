#!/bin/bash
version=1.1.1-SNAPSHOT

imagename=$1

echo "版本号${version}"
echo "服务名${imagename}"

docker pull hub.fosung.com/ksh/${imagename}:${version}


docker stop ${imagename}-$3

docker rm ${imagename}-$3

logdir="/data/logs/${imagename}/"
mkdir -p "${logdir}"

docker run --restart=always --net=host --name=${imagename}-$3    \
	-dti -v ${logdir}:${logdir} \
	-e SPRING_PROFILES_ACTIVE=dev   \
	-e SERVER_PORT=$2   \
	hub.fosung.com/ksh/${imagename}:${version}

