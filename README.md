# RoundTabler
A security tool for scanning relational database column contents for unencrypted protected card and ACH information

## Features

* Easy scanning of columns
* Low false-positives
* Avoids bad outcome (we do not certify databases with unencrypted data)
* Reported data includes confidence scores
* Quick Deployment Option
* Deployable via a docker/VM image - NOTHING TO BUILD OR INSTALL!


## Releases

Release Date	|	Version		| Change(s)
----------------|-----------------------|------------------------------------------------------------------------------------
02 Nov 2020	|	0.0.0.1     |	Repository Created. 

## Authors
Name                        | Contribution(s)
-------------------------   | ---------------------------------------------
Tiffany Ambrose             | Writer
Joseph Charlton             | Writer and Coder
Dania Jean-Baptiste         |
Brian Lasher                | Writer and Coder
Nataliz Martinez-Santiago   | Writer
Jordan Mcmillian            | Writer and Coder
Scott Piersall              | Writer and Coder
Zachary Russell             | Writer and Coder
Matthew Tyroler             | Writer
Ariel Turnley               |

## Software Requirements

RoundTabler requires you to have a working version of Docker on your machine.

##Getting Started

Clone the repository to your machine.

Double-click on the 'LaunchRoundTablerInstance.bat' to automatically build and start the Docker container.

Open Docker Desktop and open the CLI on the instance named 'roundtabler'

Navigate to the src directory using:

	`cd src`

To review the list of options pleas see:

	`java RoundTabler.RoundTable --help`