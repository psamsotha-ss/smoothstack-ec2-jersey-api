# Smoothstack AWS EC2 API

This is a project for the purpose of learning to work with EC2 instances.
The project should be pulled into an EC2 instance, built, and deployed using EC2 user data.


## Create EC2 Instance

### User Data

```shell
#!/bin/bash
sudo yum install -y java-1.8.0-openjdk-devel git
git clone https://github.com/psamsotha-ss/smoothstack-ec2-jersey-api.git
cd smoothstack-ec2-jersey-api
./mvnw clean package
java -jar target/smoothstack-ec2-jersey-api.jar
```

### Security Group

* Add TCP port 8080 inbound rule. The server is listening on port 8080

## Access API

```shell
$ curl <ec2-url>:8080/hello
Hello, Smoothstack!
```
