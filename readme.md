# Resource Management Application

## Development info - Running the application
* Install and start MongoDB
* Run fillDb.bat script to import some test data to MongoDB (Example: fillDb.bat "C:\Program Files\MongoDB\Server\4.0\bin")
* Make your code changes
* Run a mvn clean install
* Run the deploy.bat script

## Client info
* [Subscribing for Sub-units Updates](/src/main/doc/subscribe.md)
* [Editing a Sub-unit](/src/main/doc/edit.md)
* [Updating the Status for a Resource](/src/main/doc/updatestatus.md)
* [Getting the log for a resource](/src/main/doc/getLog.md)
* [Adding a Sub-unit](/src/main/doc/add.md)
* [Deleting a Sub-unit](/src/main/doc/delete.md)

# Deployment for Windows environments
* build frontend distributable
``` bash
    :isu-resource-management-client$ mvn clean install
```
* build application distributable
``` bash
    :resource-management$ mvn clean install -DskipTests
```
The generated bundle can be found at target/'resource-management-windows-native-*.zip' 

* extract target/resource-management-windows-native-*.zip
* continue with the instructions found in the extracted file named README.md

## Deployment for Unix(Linux) environments
* build frontend distributable
``` bash
    :isu-resource-management-client$ mvn clean install
```
* build backend distributable and all docker images
``` bash
    :resource-management$ mvn clean install -DskipTests
```
* start DB(mongo) container
``` bash
    :resource-management$ mvn docker:start -Pdb
```
* verify DB started successfully (check container logs)
``` bash
    :$ docker logs -f mongodb
```
* start application containers (backend & frontend)
``` bash
    :resource-management$ mvn docker:start -Papps
```
* verify apps started successfully (check container logs)
``` bash
    :$ docker logs -f backend
    :$ docker logs -f frontend
```