#!/bin/bash
mvn clean install -Dmaven.test.skip=true

#######################
echo "开始打包ksh-business"

cd ksh-business/ksh-duty-monitor
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-business/ksh-meeting
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-business/ksh-organization
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-business/ksh-surface
mvn clean package -DskipTests -Pjar-docker

######################
echo "开始打包ksh-cloud"

cd ../..

cd ksh-cloud/ksh-admin
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-cloud/ksh-eureka
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-cloud/ksh-sleuth
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-cloud/ksh-zuul
mvn clean package -DskipTests -Pjar-docker

#################################
echo "开始打包ksh-infrastructure"

cd ../..

cd ksh-infrastructure/ksh-aiface
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-infrastructure/ksh-attachment
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-infrastructure/ksh-meeting-control
mvn clean package -DskipTests -Pjar-docker

cd ../..

cd ksh-infrastructure/ksh-monitor
mvn clean package -DskipTests -Pjar-docker

###############################
echo "开始打包ksh-sys"

cd ../..
cd ksh-sys/ksh-oauth2-server
mvn clean package -DskipTests -Pjar-docker



cd ../..
cd ksh-sys/ksh-oauth2-client
mvn clean package -DskipTests -Pjar-docker

cd ../..
cd ksh-sys/ksh-sys-web
mvn clean package -DskipTests -Pjar-docker
