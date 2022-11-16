FROM ubuntu:latest

USER root

RUN apt update -y
RUN apt install default-jre -y
RUN apt install default-jdk -y

WORKDIR ./RoundTabler

COPY ./ .

RUN CLASSPATH=./:/Roundtabler/Drivers/bson-4.8.0-jar:/RoundTabler/Drivers/mariadb.jar:/RoundTabler/Drivers/mongo.jar:/RoundTabler/Drivers/mysql.jar
