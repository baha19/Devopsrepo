pipeline {
    agent any

    environment {
        MYSQL_DATABASE = 'foyer'
        DOCKER_HUB_REPO = 'yasmine064/aguiliyasmine_5sim2_g6_foyer'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
        NEXUS_URL = 'http://192.168.1.26:8081'
        GROUP_ID = 'tn.esprit.spring'
        ARTIFACT_ID = 'Foyer'
        VERSION = '0.0.1'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                 git branch: 'yasmineaguili-5SIM2-G6', url: 'https://github.com/baha19/Devopsrepo.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
                unsuccessful {
                    echo "Tests failed but proceeding to next steps."
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Delete Existing Release from Nexus') {
            steps {
                script {
                    echo "Deleting existing release artifact from Nexus, if any..."
                    sh """
                        curl -v -X DELETE -u ${env.NEXUS_CREDENTIALS_ID_USR}:${env.NEXUS_CREDENTIALS_ID_PSW} \
                        ${NEXUS_URL}/repository/maven-releases/${GROUP_ID.replace('.', '/')}/${ARTIFACT_ID}/${VERSION}
                    """
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    echo "Deploying to Nexus Release Repository..."

                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: "${NEXUS_URL}",
                        repository: 'maven-releases', 
                        credentialsId: 'nexus-credentials',
                        groupId: GROUP_ID,
                        artifactId: ARTIFACT_ID,
                        version: VERSION, 
                        artifacts: [
                            [
                                artifactId: ARTIFACT_ID,
                                classifier: '',
                                file: "target/${ARTIFACT_ID}-${VERSION}.jar",
                                type: 'jar'
                            ]
                        ]
                    )

                    echo "Deployment to Nexus Release Repository completed!"
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    docker.build("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}")
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials-id') {
                        docker.image("${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh "docker-compose down && docker-compose up -d"
            }
        }
    }

    post {
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
