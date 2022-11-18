#!/bin/bash
docker stop roundtabler-db-1
echo Stopped any existing mariadb docker instances

docker rm roundtabler-db-1
echo Removed any existing mariadb docker instances

docker stop roundtabler-adminer-1
echo Stopped any existing mariadb adminer docker instances

docker rm roundtabler-adminer-1
echo Removed any existing mariadb adminer docker instances

docker stop roundtabler
echo Stopped any existing roundtabler instance

docker rm roundtabler
echo Removed any existing roundtabler instance

docker compose -f mariadbstack.yml up -d
echo Started Docker Application stack with Mariadb database and Adminer for Browser connection ability

docker network create dbNetwork
echo Creating closed Docker Network
echo Docker Network Name: dbNetwork

docker network connect dbNetwork roundtabler-db-1
echo Added Mariadb database container to dbNetwork

docker build --tag roundtabler .

echo "Docker instance successfully built. Start container?"

docker run -d -t --name roundtabler roundtabler

docker network connect dbNetwork roundtabler
echo Added roundtabler container to dbNetwork
echo To connect to each container using the instance name in place of an IP address

printf "\nRoundTabler Testing Network Successfully built.\n\n"