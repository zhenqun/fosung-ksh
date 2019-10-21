#!/bin/bash
version=$2-SNAPSHOT

imagename=$1

echo "版本号${version}"
echo "服务名${imagename}"

docker pull hub.fosung.com/ksh/${imagename}:${version}


docker stop ${imagename}

docker rm ${imagename}

logdir="/data/logs/${imagename}/"
mkdir -p "${logdir}"

docker run --restart=always --net=host --name=${imagename}    \
	-dti -v ${logdir}:${logdir} \
	-e SPRING_PROFILES_ACTIVE=dev   \
	-e SERVER_PORT=$2   \
	hub.fosung.com/ksh/${imagename}:${version}

