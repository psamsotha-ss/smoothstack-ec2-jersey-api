#!/usr/bin/groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/psamsotha-ss/smoothstack-ec2-jersey-api.git'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*jar'
                }
            }
        }
    }
}
