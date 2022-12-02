@echo off

docker stop roundtabler-db-1
echo Stopped any existing mariadb docker instance

docker rm roundtabler-db-1
echo Removed any existing mariadb docker instance

docker stop roundtabler-adminer-1
echo Stopped any existing mariadb adminer docker instance

docker rm roundtabler-adminer-1
echo Removed any existing mariadb adminer docker instance

docker stop roundtabler-web-1
echo Stopped any existing NGINX docker instance

docker rm roundtabler-web-1
echo Removed any existing NGINX docker instance

docker stop roundtabler
echo Stopped any existing roundtabler instance

docker rm roundtabler
echo Removed any existing roundtabler instance
echo:

docker compose -f mariadbstack.yml up -d 
echo Started Docker Application stack with Mariadb database and Adminer for Browser connection ability
echo:

docker build --tag roundtabler .

echo Docker instance successfully built. Start container?

pause

docker run -d -t --name roundtabler --mount type=bind,source="%cd%",target=/RoundTabler roundtabler

echo:

docker network connect roundtabler_default roundtabler
echo Added roundtabler container to roundtabler_default
echo To connect to each container using the instance name in place of an IP address

docker exec -d -it roundtabler javac ./**/*.java ./RoundTabler/db/*.java

docker cp ./nginx.conf roundtabler-web-1:/etc/nginx/nginx.conf

echo:
echo:

echo RoundTabler Testing Network Successfully built.

echo:

pause
