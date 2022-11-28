#!/bin/bash

docker stop roundtabler-db-1
echo Stopped any existing mariadb docker instances

docker rm roundtabler-db-1
echo Removed any existing mariadb docker instances

docker stop roundtabler-adminer-1
echo Stopped any existing mariadb adminer docker instances

docker rm roundtabler-adminer-1
echo Removed any existing mariadb adminer docker instances

docker stop roundtabler-web-1
echo Stopped any existing mariadb web docker instances

docker rm roundtabler-web-1
echo Removed any existing mariadb web docker instances

docker stop roundtabler
echo Stopped any existing roundtabler instance

docker rm roundtabler
printf "Removed any existing roundtabler instance\n\n"

docker-compose-v1 -f mariadbstack.yml up -d
printf "Started Docker Application stack with Mariadb database and Adminer for Browser connection ability\n"

docker build --tag roundtabler .

echo Docker instance successfully built. Start container?

read -p "Press any key to resume ..."

docker run -d -t --name roundtabler --mount type=bind,source="$(pwd)",target=/RoundTabler roundtabler

printf "\n\n"

docker network connect roundtablerproject_default roundtabler
echo Added roundtabler container to roundtablerproject_default
echo To connect to each container using the instance name in place of an IP address

# this is not done NEED TO FIX EXEC FOR .sh

docker exec -d -it roundtabler javac RoundTabler/*.java
docker exec -d -it roundtabler javac utility/*.java
docker exec -d -it roundtabler javac RoundTabler/db/*.java

docker cp ./nginx.conf roundtabler-web-1:/usr/share/nginx/html/conf/nginx.conf

# this is not done NEED TO FIX EXEC FOR .sh

printf "\n\n"

echo RoundTabler Testing Network Successfully built.

printf "\n"

read -p "Press any key to complete the process..."