pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "" 
    }

    stages {
        stage('Get Started') {
            steps {
                echo "Start Building Pipeline"
            }
        }
        stage('Clean') {
            steps {
                echo 'Cleaning previous builds and cache...'
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the Spring Boot application...'
                sh 'mvn package'
            }
        }

        stage('Static Analysis') {
            environment {
                scannerHome = tool 'sonnarqubeScanner'
            }
            steps {
                withSonarQubeEnv(credentialsId: 'sonartoken', installationName: 'Sonarqube') {
                    sh "${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=devopsproject \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.host.url=http://10.0.2.15:9002/"
                }
            }
        }
 stage('Upload to Nexus') {
            steps {
                script {
                    echo "Deploying to Nexus..."
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '10.0.2.15:8081',
                        repository: 'back_repo',
                        credentialsId: 'nexus',
                        groupId: 'tn.esprit.spring',
                        version: '1.0.0',
                        artifacts: [
                            [
                                artifactId: 'kaddem',
                                classifier: '',
                                file: 'target/Foyer 0.0.1-SNAPSHOT.jar',
                                type: 'jar'
                            ]
                        ]
                    )
                    echo "Deployment to Nexus completed!"
                }
            }
        }
         

        stage('Docker Image') {
            steps {
                echo 'Building Docker image for Spring Boot...'
                sh 'docker build -t /app:v1.0.0 -f Dockerfile .'
            }
        }

        stage('Docker Login') {
            steps {
                echo 'Logging into DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'docker', 
                  usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh "docker login -u \$DOCKERHUB_USERNAME -p \$DOCKERHUB_PASSWORD"
                }
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'docker', 
                  usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh "docker push /-app:v1.0.0"
                }
            }
        }
         stage('Docker Compose Up') {
           steps {
               echo 'Testing application using Docker Compose...'
             // sh "docker-compose -f ./docker-compose.yml up -d"
             //  sh "docker-compose up -d"
              sh " docker-compose up -d --remove-orphans"

           }
       }

        
    } // Closing the stages block
} // Closing the pipeline block
