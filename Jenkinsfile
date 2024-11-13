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

            // Ensure the version and artifactId match with your POM file
            nexusArtifactUploader(
                nexusVersion: 'nexus3',
                protocol: 'http',
                nexusUrl: 'http://10.0.2.15:8081',
                repository: 'back_repo',  // Change this to your actual repository name in Nexus
                credentialsId: 'nexus',
                groupId: 'tn.esprit.spring',
                artifactId: 'Foyer', // Ensure this matches the artifactId in your POM
                version: '0.0.1-SNAPSHOT', // Match with version in your POM
            //    artifacts: [
             //       [
             //           artifactId: 'Foyer',
              ///          classifier: '',
              //          file: 'target/Foyer-0.0.1-SNAPSHOT.jar',  // Ensure the correct path to your artifact
             // //          type: 'jar'
             //       ]
           //     ]
            )

            echo "Deployment to Nexus completed!"
        }
    }
}
        /*stage('Deploy to Nexus') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                // Use environment variables to avoid exposing secrets in logs
                sh """
                    mvn deploy \
                    -DskipTests \
                    -Dnexus.username=\${NEXUS_USERNAME} \
                    -Dnexus.password=\${NEXUS_PASSWORD} \
                    -DrepositoryId=back_repo \
                    -Durl=http://10.0.2.15:8081/repository/back_repo
                """
            }
        }
    }
}*/


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
