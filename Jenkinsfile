pipeline {
    agent any

    triggers {
        // Déclenche le build à chaque push sur GitHub
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération du code depuis GitHub
                git branch: 'Sim2-groupe6-Zied-horbit', url: 'https://github.com/ziedhorbit/Devopsrepo.git'
            }
        }

        stage('Clean') {
            steps {
                // Nettoyage du projet
                script {
                    sh 'mvn clean'
                }
            }
        }

        stage('Test') {
            steps {
                // Exécution des tests
                script {
                    sh 'mvn test'
                }
            }
        }

        stage('Build') {
            steps {
                // Construction du livrable sans les tests
                script {
                    sh 'mvn package -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo 'Build terminé avec succès!'
        }
        failure {
            echo 'Le build a échoué.'
        }
    }
}
