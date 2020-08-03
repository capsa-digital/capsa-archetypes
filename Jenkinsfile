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
                                  --tag gcr.io/capsa-digital/capsa-infra-command:$BUILD_NUMBER \
                                  --tag gcr.io/capsa-digital/capsa-infra-command:latest .'
                    sh 'docker push gcr.io/capsa-digital/capsa-infra-command:$BUILD_NUMBER'
                    sh 'docker push gcr.io/capsa-digital/capsa-infra-command:latest'
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
        stage('Deploy Command Docker Container') {
            steps {
                sh 'kubectl create deployment command-app --image=gcr.io/capsa-digital/capsa-infra-command:$BUILD_NUMBER'
                sh 'kubectl scale deployment command-app --replicas=3'
                sh 'kubectl autoscale deployment command-app --cpu-percent=80 --min=1 --max=5'
                sh 'kubectl set env deployment/command-app --env “SPRING_PROFILES_ACTIVE=dev”'
                sh 'kubectl get pods'
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