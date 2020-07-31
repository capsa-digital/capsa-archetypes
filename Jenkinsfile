pipeline {
    agent any
    stages {
        stage('create cluster') {
            steps {
                sh 'echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list'
                sh 'sudo apt-get install apt-transport-https ca-certificates gnupg'
                sh 'curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -'
                sh 'sudo apt-get update && sudo apt-get install google-cloud-sdk'
                sh 'sudo apt-get install kubectl'
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