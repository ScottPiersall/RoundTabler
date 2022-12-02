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
Joseph Charlton             | Writer
Dania Jean-Baptiste         | Writer
Brian Lasher                | Writer and Coder
Nataliz Martinez-Santiago   | Writer
Jordan McMillan             | Writer and Coder
Scott Piersall              | Writer and Coder
Zachary Russell             | Writer and Coder
Matthew Tyroler             | Writer
Ariel Turnley               | Writer

## Software Requirements

RoundTabler requires you to have a working version of Docker on your machine.

## Getting Started

### On Windows:

1. Clone the repository to your machine.

    * `git clone https://github.com/ScottPiersall/RoundTabler.git`
    
2. Double-click on the `WindowsLaunchRoundTablerTestingNetwork.bat` to automatically build and start the RoundTabler Docker container and the Docker Application Stack.
3. Open Docker Desktop and launch the CLI on the instance named 'roundtabler'.

#### You are now ready to start using RoundTabler.

### On MacOS:

1. Clone the repository to your machine.

    * `git clone https://github.com/ScottPiersall/RoundTabler.git`
    
2. Double-click on the `MacOSLaunchRoundTablerTestingNetwork.sh` to automatically build and start the RoundTabler Docker container and the Docker Application Stack.
3. Open Docker Desktop and launch the CLI on the instance named 'roundtabler'.

#### You are now ready to start using RoundTabler.

### On Linux:

#### From the command-line:
1. Clone the repository to your machine

    * `git clone https://github.com/ScottPiersall/RoundTabler.git`

2. Navigate to the cloned directory: `cd RoundTabler`
3. Make the `LinuxLaunchRoundTablerTestingNetwork` script executable if necessary

    * `chmod +x LinuxLaunchRoundTablerTestingNetwork`

4. Execute the `LinuxLaunchRoundTablerTestingNetwork` script as a user with necessary Docker permissions

    * e.g. `sudo su` for a superuser
    * `./LinuxLaunchRoundTablerTestingNetwork`

5. Attach to the container with an interactive prompt **OR** set your host CLASSPATH to that seen in the `Dockerfile`

    * `docker exec -it roundtabler /bin/bash`

#### You are now ready to start using RoundTabler.

## Running the RoundTabler Tool

Before you run the tool, you can view the homepage provided by the NGINX Docker Container created at:

	`localhost:8000/home.html`

On the homepage, all the results from the scans will be made available to you via the localhost, as well as any errors that occur.

To review the list of helpful options in the CLI please run:

	`java RoundTabler.RoundTable --help`

Example command for RoundTabler:

	`java RoundTabler.RoundTable --type=all --dbtype=mariadb --server=[DATABASE SERVER ADDRESS] --user=[USERNAME CREDENTIAL] --password=[PASSWORD CREDENTIAL] --database=[DATABASE NAME]`

## Testing RoundTabler

The RoundTabler development team provides an example database that can be used to ensure RoundTabler is functioning correctly. With the creation of the Docker Application Stack, it comes with a mariadb and Adminer container. The steps to test against this sample database with known PCI and NACHA information are as follows.

1. First the mariadb database needs to be populated

    * Navigate to `localhost:8080` where you will see the Adminer login. 
    * Log in using
        * User: root
        * Password: example
    * Click 'Create Database' and make a database called **estore**.
    * Click 'import' in the sidebar and click 'browse'
        * Select the file under the 'SampleDBs' folder in the cloned repository on your machine named: **estore_large.mysqlbackup.sql.gz**.
        * Click `execute`
    * After some time, Adminer will reflect the changes it made. The database has been successfully populated!
    
2. Now you can open Docker Desktop, and launch the CLI for the 'roundtabler' container.
    * **On Linux**, you can access the CLI from your terminal using:
        * `docker exec -it roundtabler /bin/bash`
    
4. Once in src you can run RoundTabler:

    * `java RoundTabler.RoundTable --type=all --dbtype=mariadb --server=roundtabler-db-1 --user=root --password=example --database=estore`
    
5. This will start the RoundTabler tool and scan the estore database you recently created, and populated, in step one. 
6. Once the scan is finished, you can refresh the home page at localhost:8000/home.html and the directory will have the result file from your scan for viewing.

**Note:** If you would like to test against the estore_huge please refer to the 'Testing Against Estore_Huge' section for testing that database. 

## Helpful Notes

### Result File Naming Convention

The result files generated by RoundTabler will look similar to this:

* RESULTS_estore_20221128_234049.html, *where:*
    * estore: the name of the database scanned
    * 20221128: is the date in YYYYMMDD format
    * 234049: is the time in HHMMSS
    
### The Java Heap Space

In cases where the database you wish to scan is extremely large, you may get the following error:

`java.lang.OutOfMemoryError`

To properly navigate this error, you need to allow RoundTabler to use more memory while running. In order to do that, you simply need to add the -Xms flag to the normal RoundTabler command:

`java -Xms[MEMORY AMOUNT][M | G] RoundTabler.Roundtable --type=all --dbtype=mariadb --server=[SERVER ADDRESS] --user=[USERNAME CREDENTIAL] --password=[PASSWORD CREDENTIAL] --database=[DATABASE NAME]`

This will allow RoundTabler to use more than the default amount of memory and scan larger databases. 

### Browser Caching

In some cases, your browser will stop you from being able to view new results from scans. This is caused by your browser storing cached information of the homepage and failing to display new changes to it by the NGINX container. 

To fix this issue, you can access your browsers settings, clear the cache, and then refresh the homepage.

Here are ways to clear your browser cache for a few common browsers:

* [Google Chrome](https://support.google.com/accounts/answer/32050?hl=en&co=GENIE.Platform%3DDesktop)
* [Safari](https://www.macrumors.com/how-to/clear-safari-cache/)
* [FireFox](https://support.mozilla.org/en-US/kb/how-clear-firefox-cache)
* [Opera](https://www.opera.com/use-cases/clean-browser-and-remove-trackers)
     
## Testing Against Estore_Huge

1. Open your browser and browse to localhost:8080.

    * Log in with the credentials of:
        * User: root
        * Password: example
    * Once you are logged into the database, click 'Create Database' and name the database: 'estore_huge'
    * You can close the browser for now.

2. Now, navigate to the SampleDBs folder on the host machine located withing your cloned RoundTabler code.

3. Unzip the file named `estore_huge.mysqlbackup.sql.gz` into the SampleDBs folder you are currently in.

4. Open a terminal using **Administrative Privileges** in the SampleDBs folder and run:

    * `docker cp ./estore_huge.mysqlbackup.sql roundtabler-db-1:/`
    * This will copy the unzipped sql script into the roundtabler-db-1 container. 

5. Open Docker Desktop and click the drop down on the roundtabler application stack, then open the CLI for the roundtabler-db-1 container and run:

    * `mysql -uroot -pexample estore_huge < estore_huge.mysqlbackup.sql`
    * This will pipe the sql script into the estore_huge database.
    
6. You can now open the CLI for the roundtabler container and run the following command:

    * `java RoundTabler.RoundTable --type=all --dbtype=mariadb --server=roundtabler-db-1 --user=root --password=example --database=estore_huge`
