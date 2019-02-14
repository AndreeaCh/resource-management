## Install instructions

### Requirements : 

* Windows 10 Pro/Server 64 bit, at least 2GB of RAM, at least 4GB of disk space
* Powershell v 2.0+
* internet access is necessary

### Installation instructions (Windows)

``` cmd
setup.bat <option>
```

Install directory defaults to C:\Program Files\easy-manage
Install logs can be found in the 'logs' subdirectory.

#### Options

prereq
: install chocolatey (windows package manager)

db
: install database server (mongodb)

java
: install java runtime

node
: install nodejs and package manager

app [server_address]
: install application, server address must be specified

help
: show help

#### Examples

**setup.bat app 192.169.0.112**
: installs application using exposed server IP address

### Release notes

v 0.1
