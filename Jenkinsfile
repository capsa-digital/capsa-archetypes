pipeline {
    agent { node { label 'agent' } }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build --stacktrace --scan'
            }
        }
        stage('Create Command Docker Images') {
            steps {
                withCredentials([
                    string(credentialsId: 'my-first-secret', variable: 'FIRST_SECRET')
                ])   {
                    sh '# echo "$FIRST_SECRET" | sed -E "s/\\W+/\\n/g" | hexdump -C'
                    sh 'docker build \
                                  --file capsa-infra-command/Dockerfile \
                                  --build-arg JAR_FILE=capsa-infra-command/build/libs/capsa-infra-command-latest.jar \
                                  --build-arg INFO_APP_BUILD=$BUILD_NUMBER \
                                  --tag capsa/capsa-infra-command:$BUILD_NUMBER .'
                }
            }
        }
        stage('Create Cluster') {
            input {
                message "Continue?"
            }
            steps {
                sh 'gcloud container clusters create capsa-cluster \
                --zone us-central1-a \
                --release-channel regular'
            }
        }
        stage('Delete Cluster') {
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