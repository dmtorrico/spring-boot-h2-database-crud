pipeline {

    agent any

    environment {
        SONAR_SERVER = 'sonarqube-server'
        PROJECT_KEY = 'dmtorrico_spring-boot-h2-database-crud'
        PROJECT_NAME = 'spring-boot-h2-database-crud'
    }

    tools {
        maven 'maven-default'
    }

    stages {

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Parallel') {

            parallel {

                stage('Build') {
                    steps {
                        sh 'mvn package -DskipTests'
                    }
                }

                stage('SonarCloud Analysis') {

                    steps {

                        withCredentials([string(
                            credentialsId: 'sonarqube-token',
                            variable: 'SONAR_TOKEN'
                        )]) {

                            sh '''
                                mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389:sonar \
                                -Dsonar.host.url=https://sonarcloud.io \
                                -Dsonar.organization=dmtorrico \
                                -Dsonar.projectKey=dmtorrico_spring-boot-h2-database-crud \
                                -Dsonar.projectName=spring-boot-h2-database-crud \
                                -Dsonar.token=$SONAR_TOKEN
                            '''
                        }
                    }
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
    }

    post {

        success {
            echo 'Pipeline ejecutado correctamente'
        }

        failure {
            echo 'Pipeline falló'
        }
    }
}