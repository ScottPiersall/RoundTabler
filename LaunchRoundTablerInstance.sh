#!/bin/bash
@echo off

docker stop roundtabler
echo Stopped existing roundtabler instance

docker rm roundtabler
echo Removed existing roundtabler instance

docker build --tag roundtabler .

echo Docker instance successfully built. Container about to be run.

docker run -d -t --name roundtabler roundtabler

