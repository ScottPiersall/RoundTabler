@echo off

docker stop roundtabler
echo Stopped pre-existing roundtabler instance

docker rm roundtabler
echo Removed pre-existing roundtabler instance

docker build --tag roundtabler .

echo Docker instance successfully built. Start container?
pause

docker run -d -t --name roundtabler roundtabler

docker network connect dbNetwork roundtabler

echo:
echo:

echo Fresh RoundTabler instance ready for testing with updated code.

echo:

pause