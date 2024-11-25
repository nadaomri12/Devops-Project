pipeline {
    agent any
    tools {
        maven 'Maven' // Nom de l'installation Maven configurée dans Jenkins
    }
    environment {         
        FRONTEND_TAG = "frontend-${env.BUILD_NUMBER}"         
        BACKEND_TAG = "backend-${env.BUILD_NUMBER}"  
        resourceGroup = 'resourcegroup'
        clusterName = 'oriented-cow-aks'
    }  
    stages {
        stage('Git Checkout') {
            steps {
                echo "Checking out code from GitHub..."
                checkout([$class: 'GitSCM', 
                          branches: [[name: '*/dev']],
                          extensions: [], 
                          userRemoteConfigs: [[
                              url: 'https://github.com/nadaomri12/Devops-Project.git', 
                              credentialsId: 'git-credentials'
                          ]]
                ])
                echo 'Git Checkout Completed'
            }
        }
        stage('Install Azure CLI and kubectl') {
            steps {
                script {
                    sh '''
                        # Installer Azure CLI
                        echo "Installing Azure CLI..."
                        curl -sL https://aka.ms/InstallAzureCLIDeb | bash
                        
                        # Vérifier l'installation
                        az --version
                        
                        # Installer kubectl via Azure CLI
                        echo "Installing kubectl..."
                        az aks install-cli
                        
                        # Vérifier l'installation de kubectl
                        kubectl version --client
                    '''
                }
            }
        }
        stage('Login to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            echo "Logging into Docker Hub..."
                            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        '''
                    }
                }
            }
        }
        stage('Build and Push Frontend Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        docker.withRegistry('', 'DockerHubCredentials') {
                            def frontendImage = docker.build("nadaomri2001/coaudit-frontend:${FRONTEND_TAG}", "-f coAudit-frontend/Dockerfile coAudit-frontend")
                            frontendImage.push()
                            frontendImage.push('latest')
                        }
                    }
                }
            }
        }
        stage('Build and Push Backend Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        docker.withRegistry("https://index.docker.io/v1/", 'DockerHubCredentials') {
                            def backendImage = docker.build("nadaomri2001/coaudit-backend:${BACKEND_TAG}", "-f coAudit-backend/Dockerfile coAudit-backend")
                            backendImage.push()
                            backendImage.push('latest')
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
