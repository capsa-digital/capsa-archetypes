pipeline {
    agent { node { label 'agent' } }
    stages {
        stage('create cluster') {
            steps {
                sh 'gcloud container clusters create capsa-cluster \
                --zone us-central1-a \
                --release-channel regular'
            }
        }
        stage('delete cluster') {
            steps {
                sh 'gcloud container clusters delete capsa-cluster \
                --zone us-central1-a --quiet'
            }
        }
    }
}