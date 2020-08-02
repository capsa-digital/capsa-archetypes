pipeline {
    agent { node { label 'agent' } }
    stages {
        stage('build') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'my-first-secret',
                        variable: 'my-first-secret')
                ])   {
                    sh 'echo $my-first-secret'
                }
            }
        }
        stage('create cluster') {
            input {
                message "Continue?"
            }
            steps {
                sh 'gcloud container clusters create capsa-cluster \
                --zone us-central1-a \
                --release-channel regular'
            }
        }
        stage('delete cluster') {
            input {
                message "Continue?"
            }
            steps {
                sh 'gcloud container clusters delete capsa-cluster \
                --zone us-central1-a --quiet'
            }
        }
    }
}