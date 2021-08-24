#!/usr/bin/groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/psamsotha-ss/smoothstack-ec2-jersey-api.git', branch: 'main'
            }
        }
        stage('SonarQube Analysis') {
            environment {
                SONARQUBE_TOKEN = credentials('sonarqube-token')
            }
            steps {
                sh "./mvnw sonar:sonar \\\n" +
                        "  -Dsonar.projectKey=jersey-api \\\n" +
                        "  -Dsonar.host.url=http://sonarqube:9000 \\\n" +
                        "  -Dsonar.login=${SONARQUBE_TOKEN}"
            }
        }
        stage('Test') {
            steps {
                sh './mvnw clean test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*jar'
                }
            }
        }
        stage('S3 Archive Upload') {
            steps {
                withAWS(region: 'us-west-2', credentials: 'SmoothstackAws') {
                    s3Upload(bucket: 'psamsotha-smoothstack', file: 'target/smoothstack-ec2-jersey-api.jar',
                            path: 'devops-training/smoothstack-ec2-jersey-api.jar')
                }
            }
        }
        stage('Pull Archive') {
            steps {
                withAWS(region: 'us-west-2', credentials: 'SmoothstackAws') {
                    s3Download(bucket: 'psamsotha-smoothstack', file: '/home/smoothstack-ec2-jersey-api.jar',
                            path: 'devops-training/smoothstack-ec2-jersey-api.jar', force: true)
                }
            }
        }
        stage('Deploy') {
            steps {
                sh 'java -jar /home/smoothstack-ec2-jersey-api.jar &'
            }
        }
    }
}
