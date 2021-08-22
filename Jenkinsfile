#!/usr/bin/groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/psamsotha-ss/smoothstack-ec2-jersey-api.git', branch: 'main'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw clean test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('Code Quality') {
            steps {
                sh './mvnw validate'
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
    }
}
