@echo off

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
echo:
docker compose -f mariadbstack.yml up -d 
echo Started Docker Application stack with Mariadb database and Adminer for Browser connection ability
echo:
docker network create dbNetwork
echo Creating closed Docker Network
echo Docker Network Name: dbNetwork
echo:
docker network connect dbNetwork roundtabler-db-1
echo Added Mariadb database container to dbNetwork
echo:
docker build --tag roundtabler .

echo Docker instance successfully built. Start container?

pause

docker run -d -t --name roundtabler --mount type=bind,source="%cd%",target=/RoundTabler roundtabler

echo:

docker network connect dbNetwork roundtabler
echo Added roundtabler container to dbNetwork
echo To connect to each container using the instance name in place of an IP address

docker exec -d roundtabler javac RoundTabler/*.java
docker exec -d roundtabler javac utility/*.java
docker exec -d roundtabler javac RoundTabler/db/*.java

echo:
echo:

echo RoundTabler Testing Network Successfully built.

echo:

pause
