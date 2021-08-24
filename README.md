# Smoothstack AWS EC2 API

This project is to be deployed to an EC2 instance. The CI/CD will be handled by Jenkins.
Jenkins and SonarQube should be run on the same instance. After pushing to GitHub, a webhook should trigger a build on Jenkins, and the app should then be deployed.

## CI/CD Steps

The CI steps are as follows:

1. Checkout code
2. Static analyze the code
3. Run unit tests
4. Run integration tests
5. Build the project
6. Upload the artifact to S3

The CD steps are as follows:

1. Pull the artifact from S3
2. Run the app
   

## CI/CD Setup

### Create EC2 Instance

Everything will be run on an Amazon Linux 2 instance.

1. Install Docker
2. Install Jenkins
3. Install SonarCube

### Setup Docker on EC2 (user data)

```shell
#!/bin/bash
yum install -y docker
service docker start
usermod -a -G docker ec2-user
docker network create jenkins-sonar
docker pull jenkins/jenkins:lts-jdk11
docker run -d --net jenkins-sonar -v jenkins_home:/var/jenkins_home \
       -p 8080:8080 -p 50000:50000 -p 8000:8000 --name jenkins \
       jenkins/jenkins:lts-jdk11
docker pull sonarqube
docker run -d --net jenkins-sonar --name sonarqube -p 9000:9000 sonarqube
```

#### Security Group

* Add TCP port 8080 inbound rule for Jenkins access
* Add TCP port 9000 inbound rule for SonarQube access
* Add TCP port 8080 inbound rule. The API server is listening on port 8000

### Setup Jenkins

1. Go to `<ec2-domain>:8080`
2. Check docker logs `docker logs jenkins` and get the password to set up Jenkins
3. Run Jenkins set up and create a user
4. Add the "Pipeline: AWS Steps" plugin

### Setup SonarQube

1. Go to `<ec2-domain>:9000`
2. Use username `admin` and password `admin`
3. Create a new password

## Jenkins Pipeline

### Setup GitHub Webhook

1. In Jenkins, click on the user on the top-right.
2. In the left sidebar, click Configure
3. Add a new API token and give it a name like `github-webhook`
4. Copy the token
5. Go to the project on GitHub and go to Settings and Webhooks
6. Create a new webhook and user the URL `<jenkins-url>/github-webhook/`
7. Paste the copied token into the "Secret" field
8. In Jenkins, create a pipeline and select "GitHub hook trigger for GITScm polling" from Build Triggers
9. Test by making a commit. (Note: the pipeline must be first ran manually once before a commit will trigger it)

### Setup SonarQube project

1. Create a new project manually
2. Select "Locally" when asked how to analyze repository
3. Create a token to pass to Jenkins
4. In Jenkins, go to Manage Jenkins -> Manage credentials and create a new secret to use as an environment variable (See Jenkinsfile for usage)


## Access API

```shell
$ curl <ec2-url>:8000/hello
Hello, Smoothstack!
```
