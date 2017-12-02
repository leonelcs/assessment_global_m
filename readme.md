Hi,

this is a simple scheduled call for health check, there is a start.sh to run it on background, the response will be set on a map counting the response code and the number of time it happened since the script is started.

First step - run mvn package to generate the .jar file.

second step - run the start.sh using ./start.sh in the root folder of the project

check the log.txt

There is also a stop script in to stop the process.


