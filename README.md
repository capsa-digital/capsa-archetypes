# glome-capsa-infra

## Install Jenkins Master

### Create VM Instance

**Name**

jenkins-master

**Machine type**

e2-standard-4 (4 vCPUs, 16 GB memory)

**Boot disk**

50GB

**OS**

Ubuntu 20.04 LTS

**Cloud API access scopes**

Allow full access to all Cloud APIs

### Install Java
#### Upload and extract
```
> sudo apt update && sudo apt upgrade
> sudo apt install default-jdk
```

#### Update .bashrc - append the foloowing
```
export JAVA_HOME=~/jdk-11
export PATH=$JAVA_HOME/bin:$PATH
```

#### Resource .bashrc
```
. .bashrc
```

### Install docker
```
sudo apt install docker.io
sudo systemctl enable --now docker
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
docker run hello-world
```
see https://docs.docker.com/engine/install/ubuntu/

### Install Jenkins
```
docker network create jenkins
docker volume create jenkins-docker-certs
docker volume create jenkins-data
docker container run --name jenkins-docker --detach --restart unless-stopped \
  --privileged --network jenkins --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 docker:dind 
docker container run --name jenkins-blueocean --detach --restart unless-stopped \
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  --publish 8080:8080 --publish 50000:50000 jenkinsci/blueocean
```
see https://www.jenkins.io/doc/book/installing/

###  Create firewall rule 

**Target tags**

http-server https-server

**IP ranges**

0.0.0.0/0

**Protocols and ports**

tcp:8080 

### Setup Jenkins

#### Create root user (see initial password inside docker log)

#### Create new GitHub Organization

- name glome-capsa
- add github Credentials user/password 
    - go to https://github.com/settings/tokens to generate token and use token as password
    - enter 'glome-capsa-jenkins-user' as user

### Install gcloud

```
echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
sudo apt-get install apt-transport-https ca-certificates gnupg
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -
sudo apt-get update && sudo apt-get install google-cloud-sdk
sudo apt-get install kubectl
gcloud init --console-only
```
see https://cloud.google.com/sdk/docs/downloads-apt-get

### Connect agent

TODO should we use agent over SSH instead of JNLP? see - https://www.howtoforge.com/tutorial/ubuntu-jenkins-master-slave/
TODO should we create Docker with Java, gcloud, kubectl? Where these Docker containers run?

#### Upload agent.jar to /usr/lib

Create a new node in Jenkins UI and follow the on-screen instructions to download agent.jar

#### Run agent
Create jenkins-agent.service
```
sudo nano /etc/systemd/system/jenkins-agent.service
```
with content
```
[Unit]
Description=Jenkins Agent
[Service]
WorkingDirectory=/usr/sbin
ExecStart=/usr/sbin/jenkins-agent
TimeoutStopSec=30
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
```
Create jenkins-agent
```
sudo nano /usr/sbin/jenkins-agent
```
with content
```
#!/bin/sh
sudo /usr/bin/java -jar /usr/lib/agent.jar -jnlpUrl http://10.128.0.6:8080/computer/agent/slave-agent.jnlp -secret d6dbd38a39f6eeca182d9b2e9c696947e3a4e2732c670912444d0ef54781f6c7 -workDir "~/jenkins"
```

```
sudo chmod u+x /usr/sbin/jenkins-agent
sudo systemctl daemon-reload
sudo systemctl enable jenkins-agent.service
sudo systemctl start jenkins-agent
sudo systemctl status jenkins-agent
```
to see logs
```
sudo journalctl -f -n 1000 -u jenkins-agent
```
to stop
```
sudo systemctl stop jenkins-agent
```


