pipeline {
    agent {
        docker {
            image 'ubuntu:20.04'
        }
    }
    stages {
        stage('create cluster') {
            steps {
                sh 'echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list'
                sh 'apt-get install apt-transport-https ca-certificates gnupg'
                sh 'curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -'
                sh 'apt-get update && sudo apt-get install google-cloud-sdk'
                sh 'apt-get install kubectl'
                sh 'gcloud init --console-only'
                sh 'gcloud container clusters create glome-capsa-cluster --zone us-central1-a --release-channel regular'
            }
        }
        stage('delete cluster') {
            steps {
                sh '# gcloud container clusters delete glome-capsa-cluster --zone us-central1-a --quiet'
            }
        }
    }
}