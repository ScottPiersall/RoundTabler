@echo off

docker stop roundtabler
echo Stopped existing roundtabler instance

docker rm roundtabler
echo Removed existing roundtabler instance

docker build --tag roundtabler .

echo Docker instance successfully built. Start container?
pause

docker run -d -t --name roundtabler roundtabler

docker exec -it roundtabler bash "cd src && javac RoundTabler/*.java && java RoundTabler.RoundTable --help"

